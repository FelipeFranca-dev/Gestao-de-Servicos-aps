package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import dao.Dao;
import entities.Servico;
import view.View;

public class Controller {
	
	Dao model;
	View view;
	
	public Controller(Dao aModel, View aView) {
		this.model = aModel;
		this.view = aView;
		
	}
}
