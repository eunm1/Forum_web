package com.eunm1.forum.controller;

import com.eunm1.forum.config.MyMemberDetail;
import com.eunm1.forum.db.entity.Member;
import com.eunm1.forum.db.other.RSA;
import com.eunm1.forum.dto.ArticleDto;
import com.eunm1.forum.dto.FileDto;
import com.eunm1.forum.dto.MemberDto;
import com.eunm1.forum.service.ArticleService;
import com.eunm1.forum.service.FileService;
import com.eunm1.forum.service.MemberService;
import com.eunm1.forum.util.MD5Checksum;
import com.eunm1.forum.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/forum")
public class ForumController {

    @Autowired //의존성 주입 어노테이션
    private ArticleService articleService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private FileService fileService;

    private RSAUtil rsaUtil = new RSAUtil();

    private MyMemberDetail user;

    @GetMapping("/main")
    public ModelAndView forumView(ModelAndView modelAndView, @RequestParam(value="page", defaultValue = "1") Integer pageNum,
                                  HttpServletRequest request){
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if(flashMap == null || flashMap.get("loginMsg") != null){ // redirect로 넘어온 값이 있다면
            List<ArticleDto> articleDtoList = articleService.getArticleList(pageNum);
            modelAndView.addObject("articleList", articleDtoList);

            Integer[] pageList = articleService.getPageList(pageNum);
            modelAndView.addObject("pageList", pageList); //전체 페이지 리스트
            modelAndView.addObject("page",pageNum); //현재 페이지
            modelAndView.addObject("lastPageNo", articleService.getTotalLastPageNum());
            modelAndView.addObject("list", fileService.getFilenameList(articleDtoList));
        }

        //login 페이지를 따로 만들지 않았기 때문에 메인 페이지에서 pk값을 받아온다
        HttpSession session = request.getSession();
        PrivateKey key = (PrivateKey) session.getAttribute("PK");
        if(key != null)
            session.removeAttribute("PK"); //기존 키 파기
        RSA rsa = rsaUtil.createRSA();

        modelAndView.addObject("modulus", rsa.getModulus());
        modelAndView.addObject("exponent", rsa.getExponent());
        session.setAttribute("PK", rsa.getPrivateKey());


        //if session 값이 없다면 rsa키 생성
        modelAndView.setViewName("main_forum");
        return modelAndView;
    }

    @PostMapping("/signup")
    public RedirectView Signup(MemberDto memberDto, HttpSession session) throws Exception {
        /*rsa 인증하기*/
        PrivateKey privateKey = (PrivateKey) session.getAttribute("PK");

        if(privateKey == null){
            throw new BadCredentialsException("비정상적인 접근");
        }else{
            memberDto.setLoginId(rsaUtil.getDecryptText(privateKey, memberDto.getLoginId()));
            memberDto.setLoginPw(rsaUtil.getDecryptText(privateKey, memberDto.getLoginPw()));
        }
        memberService.saveMember(memberDto);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/forum/main");
        return redirectView;
    }

    @GetMapping("/login")
    public RedirectView login_page(HttpSession session, RedirectAttributes redirectAttributes){

        redirectAttributes.addFlashAttribute("loginMsg","need");

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/forum/main");
        return redirectView;
    }

    @GetMapping("/login-ok")
    public RedirectView login_success(RedirectAttributes redirectAttributes){

        user = (MyMemberDetail)SecurityContextHolder.getContext().getAuthentication().getDetails();
        redirectAttributes.addFlashAttribute("loginMsg","success");

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/forum/main");
        return redirectView;
    }

    @PostMapping("/login-fail")
    public RedirectView login_failed(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("loginMsg","fail");
        //return new ModelAndView("redirect:/forum");
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/forum/main");
        return redirectView;
    }

    @GetMapping("/logout")
    public RedirectView logout(RedirectAttributes redirectAttributes){
        user = null;

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/forum/main");
        return redirectView;
    }

    @GetMapping("/search")
    public RedirectView search_article(RedirectAttributes redirectAttributes, @RequestParam(value="search_radio") String ratio_num,
                                       @RequestParam(value = "keyword") String keyword){

        List<ArticleDto> articleDtoList = articleService.searchArticle(Integer.parseInt(ratio_num), keyword);

        RedirectView redirectView = new RedirectView("/forum/main",true);
        redirectAttributes.addFlashAttribute("articleList",articleDtoList);
        return redirectView;
    }

    @GetMapping("/add_article")
    public ModelAndView add_forum(ModelAndView modelAndView){
        modelAndView.setViewName("add_forum");
        return modelAndView;
    }
    @PostMapping("/add_article/add")
    public RedirectView add_btn(@RequestParam("file") MultipartFile file, ArticleDto articleDto){

        FileDto fileDto = fileService.FileCheck(file);
        if(fileDto != null){
            articleDto.setFileId(fileService.saveFile(fileDto));
        }
        articleDto.setMid(String.valueOf(user.getId()));
        articleService.saveArticle(articleDto);

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/forum/main");
        return redirectView;
    }

    @GetMapping("/detail_article/{id}")
    public ModelAndView detail_form(@PathVariable("id") Long aid, ModelAndView modelAndView){
        if(user != null){
            modelAndView.addObject("Id", user.getId());
        }

        ArticleDto articleDto = articleService.getArticle(aid);
        //html에 입력
        modelAndView.addObject("article", articleDto);


        Long mid = articleService.findMidById(aid);
        modelAndView.addObject("mid", mid);

        String filepath = fileService.getFile(articleDto.getFileId()).getFileName();
        modelAndView.addObject("imgPath", filepath);

        modelAndView.setViewName("detail_forum");
        return modelAndView;
    }

    @GetMapping("/detail_article/update_article/{id}")
    public ModelAndView update_form(@PathVariable("id") Long aid, ModelAndView modelAndView){
        ArticleDto articleDto = articleService.getArticle(aid);

        String filename = fileService.getFile(articleDto.getFileId()).getOrignFilename();

        modelAndView.addObject("article", articleDto);
        modelAndView.addObject("fileName", filename);

        String filepath = fileService.getFile(articleDto.getFileId()).getFileName();
        modelAndView.addObject("imgPath", filepath);

        modelAndView.setViewName("update_forum");
        return modelAndView;
    }

    @PutMapping("/detail_article/update_article/edit/{id}")
    public RedirectView update_article(@PathVariable("id") Long aid, @RequestParam("file") MultipartFile file,
                                       ArticleDto articleDto){

        Long origFileId = articleService.findFileIdById(aid);

        if(!file.getOriginalFilename().isEmpty()){
            //기존 이미지 삭제
            fileService.deleteFile(origFileId);


            //새 이미지 저장
            FileDto fileDto = fileService.FileCheck(file);
            articleDto.setFileId(fileService.saveFile(fileDto));
        }else{
            articleDto.setFileId(origFileId);
        }

        articleDto.setMid(String.valueOf(articleService.findMidById(aid)));

        articleService.saveArticle(articleDto);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/forum/detail_article/"+aid);
        return redirectView;
    }

    @DeleteMapping("/detail_article/delete_article/{id}")
    public RedirectView delete_article(@PathVariable("id") Long aid){
        Long origFileId = articleService.findFileIdById(aid);
        if(origFileId != null)
            fileService.deleteFile(origFileId);
        articleService.deleteArticle(aid);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/forum/main");
        return redirectView;
    }

    @GetMapping("/develop_env")
    public ModelAndView develop_env(ModelAndView modelAndView){
        modelAndView.setViewName("develop_env");
        return modelAndView;
    }

    @GetMapping("/develop_pro")
    public ModelAndView develop_pro(ModelAndView modelAndView){
        modelAndView.setViewName("develop_pro");
        return modelAndView;
    }
}
