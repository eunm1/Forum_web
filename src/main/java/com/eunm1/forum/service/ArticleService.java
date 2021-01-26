package com.eunm1.forum.service;

import com.eunm1.forum.db.entity.Article;
import com.eunm1.forum.db.entity.Member;
import com.eunm1.forum.db.repository.ArticleRepository;
import com.eunm1.forum.db.repository.MemberRepository;
import com.eunm1.forum.dto.ArticleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private MemberRepository memberRepository;

    private static final int Article_Count_inPage = 10; //한 페이지에 존재하는 게시물 수
    private static final int Block_Page_Count = 5; //블럭에 존재하는 페이지 수
    private Integer totalLastPageNum = 1;

    @Transactional
    public Long saveArticle(ArticleDto articleDto){

        return articleRepository.save(articleDto.toEntity()).getId();
    }

    @Transactional
    public List<ArticleDto> getArticleList(Integer pageNum){

        /* 페이지 별로 게시물 가져오기 */
        Page<Article> page = articleRepository.findAll(PageRequest
                .of(pageNum-1, Article_Count_inPage, Sort.by(Sort.Direction.ASC,"createdDate")));
        List<Article> articleList = page.getContent();

        //List<Article> articleList = articleRepository.findAll();
        List<ArticleDto> articleDtoList = new ArrayList<>();
        String name;
        for(Article article : articleList){
            name = findNamebyMid(article.getMid());
            ArticleDto articleDto = this.convertEntityToDto(article);
            articleDto.setMid(name);
            articleDtoList.add(articleDto);
        }
        return articleDtoList;
    }

    private ArticleDto convertEntityToDto(Article article) {
        return ArticleDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .body(article.getBody())
                .mid(String.valueOf(article.getMid()))
                .hit(article.getHit())
                .createdDate(article.getCreatedDate())
                .fileId(article.getFileId())
                .build();
    }

    @Transactional
    public ArticleDto getArticle(Long id) {
        Article article = articleRepository.findOneById(id);
        ArticleDto articleDto = this.convertEntityToDto(article);
        articleDto.setHit(article.getHit()+1);

        saveArticle(articleDto);//조회수 증가후 저장

        //작성자 이름으로 저장후 리턴
        String name = findNamebyMid(article.getMid());
        articleDto.setMid(name);
        return articleDto;
    }

    @Transactional
    public String findNamebyMid(Long mid){
        return memberRepository.findOneById(mid).getNickname();
    }

    @Transactional
    public Long findMidById(Long id){
        return articleRepository.findOneById(id).getMid();
    }

    @Transactional
    public Long findFileIdById(Long id){ return articleRepository.findOneById(id).getFileId();}

    @Transactional
    public void deleteArticle(Long id) {

        articleRepository.deleteById(id);
    }

    public Integer[] getPageList(Integer pageNum) {
        Integer[] pageList = new Integer[Block_Page_Count];

        //총 게시물 수
        Double TotalArticleCount = Double.valueOf(this.getArticleCount());
        //마지막 페이지 번호 계산 ex) total : 20, ArticleinPage : 8 => 3페이지지
        //Math.ceil(2.03) = 3
        totalLastPageNum = (int)(Math.ceil((TotalArticleCount/Article_Count_inPage)));
        //현재 페이지를 기준으로 마지막 페이지 계산(페이지 번호 표시가 5개로 제한 = Block_Page_Count)
        Integer blockLastPage = (totalLastPageNum > pageNum + Block_Page_Count) ?
                pageNum + Block_Page_Count : totalLastPageNum;

        //페이지 시작 번호 조정 => [1]2345, 23[4]56
        pageNum = (pageNum <= 3) ? 1 : pageNum-2;

        for(int val = pageNum, i=0; val <= blockLastPage; val++, i++){
            pageList[i] = val;
        }

        return pageList;
    }

    public Integer getTotalLastPageNum(){
        return totalLastPageNum;
    }

    @Transactional
    public Long getArticleCount(){
        return articleRepository.count(); // 전체 게시물 수
    }

    // http://www.devkuma.com/books/pages/566 <-  repository사용법

    @Transactional
    public List<ArticleDto> searchArticle(int ratio_num, String keyword) {
        List<Article> articleList = new ArrayList<>();
        if(ratio_num == 1)
            articleList = articleRepository.findByTitleContainingIgnoreCase(keyword);
        else if(ratio_num == 3)
            articleList = articleRepository.findByBodyContainingIgnoreCase(keyword);
        /*else if(ratio_num == 2)
            articleRepository.findByNicknameContaining(keyword);*/
        else if(ratio_num == 4)
            articleList = articleRepository.findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase(keyword, keyword);

        List<ArticleDto> articleDtoList = new ArrayList<>();
        String name;
        for(Article article : articleList){
            name = findNamebyMid(article.getMid());
            ArticleDto articleDto = this.convertEntityToDto(article);
            articleDto.setMid(name);
            articleDtoList.add(articleDto);
        }

        return articleDtoList;
    }
}
