package com.example.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DemoMapper {
    public void createTable();
    public int insertData(@Param("title") String title,@Param("author") String author);
}
