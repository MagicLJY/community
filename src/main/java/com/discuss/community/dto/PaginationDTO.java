package com.discuss.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JY Lin
 * @time -2020-05-30-18:09
 **/
@Data
public class PaginationDTO {
    private List<QuestionDTO> questions;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    private List<Integer> pages=new ArrayList<>();//当前显示的页数列表
    private Integer totalPage;

    //totalcount为问题总数，size为单页问题数,page为当前页
    public void setPagination(Integer totalCount, Integer page, Integer size) {

        //先判断总页数
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        //判断选项卡可以选择哪几页,展示前三页和后三页
        this.page=page;
        pages.add(page);
        for(int i=1;i<=3;i++){
            if(page-i>0){
                pages.add(0,page-i);
            }
            if(page+i<=totalPage){
                pages.add(page+i);
            }
        }


        //判断是否是首尾页，来决定是否展示上一页或下一页
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }
        if (page == totalPage) {
            showNext = false;
        } else {
            showNext = true;
        }
        //是否展示第一页（首页标识）
        if(pages.contains(1)){
            showFirstPage=false;
        }else{
            showFirstPage=true;
        }
        //是否展示尾页标识
        if(pages.contains(totalPage)){
            showEndPage=false;
        }else{
            showEndPage=true;
        }

    }
}
