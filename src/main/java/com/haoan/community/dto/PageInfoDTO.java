package com.haoan.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageInfoDTO<T> {

    private List<T> data;
    private boolean showPrevious;
    private boolean showNext;
    private boolean showEndPage;
    private boolean showFirstPage;
    private Integer page;
    private List<Integer> pages = new ArrayList<>();
    private Integer totalPage;


    public void setPageInfo(Integer totalCount,Integer page,Integer size){

        //判断一共几页
        if(totalCount % size == 0){
            totalPage = totalCount/size;
        }else {
            totalPage = totalCount/size+1;
        }


        //保证page不越界
        if(page<1){
            page=1;
        }
        if(page>totalPage){
            page=totalPage;
        }

        this.page= page;
        //设定显示的数组列表
        //1.加入当前页
        pages.add(page);
        //2.保证当前页后三个前三个
        for(int i=1;i<=3;i++){
            //3.放入比当前页小的元素
            if(page-i>0){
                pages.add(0,page-i);
            }
            //3.放入并当前页大的元素
            if(page+i<=totalPage){
                pages.add(page+i);
            }
        }

        //是否展示上一页
        if(page==1){
            showPrevious=false;
        }else {
            showPrevious=true;
        }

        //是否展示下一页
        if(page==totalPage){
            showNext=false;
        }else {
            showNext=true;
        }

        //是否展示第一页
        if(pages.contains(1)){
            showFirstPage=false;
        }else {
            showFirstPage=true;
        }
        //是否展示最后一页
        if (pages.contains(totalPage)){
            showEndPage=false;
        }else {
            showEndPage=true;
        }

    }
}
