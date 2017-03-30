package presentation.controller;

import business.transfer.Parameter;

public class FrontController {
	private FrontController() {
	}
	
    public static Object processRequest(String request, Parameter parameter) {
        return ApplicationController.handleRequest(request, parameter);
    }
}
