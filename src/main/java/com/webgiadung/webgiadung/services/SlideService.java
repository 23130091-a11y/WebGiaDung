package com.webgiadung.webgiadung.services;

import com.webgiadung.webgiadung.dao.SlideDao;
import com.webgiadung.webgiadung.model.Slide;

import java.util.List;

public class SlideService {
    SlideDao slideDao = new SlideDao();

    public List<Slide> getListSlide(){
        return SlideDao.getListSlide();
    }
    public Slide getById(int id) {
        return SlideDao.getById(id);
    }
    public boolean insert(Slide slide) {
        int result = SlideDao.insert(slide);
        return result > 0;
    }

}
