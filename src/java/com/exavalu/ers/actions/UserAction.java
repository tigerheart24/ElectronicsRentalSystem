/* git hub token = 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
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.exavalu.ers.actions;

import com.opensymphony.xwork2.ActionSupport;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.exavalu.ers.pojos.User;
import com.exavalu.ers.services.ReportService;
import com.exavalu.ers.services.UserService;
import com.exavalu.ers.servlets.LoginServlet;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;

/**
 *
 *
 */
public class UserAction implements SessionAware {

    private int userId;
    private String userName;
    private String userMobileNo;
    private String userEmail;
    private String password;
    private String city;
    private String country;
    private int status;
    private int roleId;
    private boolean validUser;
//    private List<Object> newProductList = null;
//    private List<Object> mostOrderedList = null;
    
    private List<Integer> itemsList = null;

    private static long serialVersionUID = 6329394260276112660L;
    private ResultSet rs = null;
    private User user = null;
    private List<User> userList = null;
    private boolean noData = false;
    private String msg = "";
    private UserService userservice = null;
    private int ctr = 0;
    private ReportService reportService = null;

    private UserService dao = new UserService();
    private String submitType;

    private boolean session = false;

    private SessionMap<String, Object> sessionMap;

//getters and setters  
    @Override
    public void setSession(Map<String, Object> map) {
        setSessionMap((SessionMap<String, Object>) (SessionMap) map);
    }

    public String execute() throws ClassNotFoundException, IOException, Exception {
        
        System.out.println(getUserEmail());
        System.out.println(getSessionMap().get("roleId"));
        setReportService(new ReportService());
        if (getSessionMap().get("roleId")== null) {
             
            setSession(true);
            User myUser = new User();
            myUser.setUserEmail(getUserEmail());
            myUser.setPassword(getPassword());
            System.out.println(getUserEmail());
            
            itemsList= UserService.showType();
            System.out.println(itemsList);
            User validuser = UserService.validateLoginCredentials(myUser);
            
            System.out.println(validuser.getUserEmail());
            if (validuser.isValidUser()) {
                getSessionMap().put("login", "true");
                getSessionMap().put("userEmail", validuser.getUserEmail());
                getSessionMap().put("roleId", validuser.getRoleId());
                System.out.println(getSessionMap().get("roleId"));

                if ((int) getSessionMap().get("roleId") == 1) {
                    return "ADMIN";
                } else {
                    
                        return "CUSTOMER";

                }
            }
            else {
                return "LOGIN";
            }
        
        }
        else {
            if ((int) getSessionMap().get("roleId") == 1) {
                return "ADMIN";
            } else {
                 
                return "CUSTOMER";
            }
        }
    }

    public String logout() {
        if (getSessionMap() != null) {
            System.out.println("session ending");
            setSession(false);
            getSessionMap().invalidate();
        }
         System.out.println("session end");
        return "LOGOUT";
    }

    private UserService us = new UserService();

    public String updateModified() throws Exception {
        try {
            System.out.println(getUserId());
            if (getSubmitType().equals("updatedata")) {
                System.out.println(getUserId());
                User user = getUs().fetchUserDetails(getUserId());
                if (user != null) {
                    setUserName(user.getUserName());
                    setPassword(user.getPassword());
                    setUserId(user.getUserId());
                    setUserMobileNo(user.getUserMobileNo());
                    setUserEmail(user.getUserEmail());
                    setCity(user.getCity());
                    setCountry(user.getCountry());
                    setStatus(user.getStatus());
                    setRoleId(user.getRoleId());
                }
            } else {
                int i = getUs().updateUserDetails(getUserId(), getUserName(), getPassword(), getUserMobileNo(), getUserEmail(), getCity(), getCountry(), getStatus(), getRoleId());
                if (i > 0) {
                    setMsg("Record Updated Successfuly");
                } else {
                    setMsg("error");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "UPDATE";
    }

    public String executeCustomer() throws Exception {
        try {
            setUserList(new ArrayList<>());
            setUserList(UserService.report());

            if (!userList.isEmpty()) {
                setNoData(false);
                System.out.println("Users retrieve = " + getUserList().size());
                System.out.println("setting nodata=false");
            } else {
                setNoData(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "REPORT";
    }
    private UserService usd = new UserService();

    public String deleteCustomer() throws Exception {
        try {
            int isDeleted = getUsd().deleteUserDetails(getUserId());
            if (isDeleted > 0) {
                setMsg("Record deleted successfully");
            } else {
                setMsg("Some error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "DELETE";
    }

    // call a java service in my server or external server.
    /*       User myUser = new User();
            myUser.setUserEmail(userEmail);
            myUser.setPassword(password);
            
            User validuser = UserService.validateLoginCredentials(myUser);
            
            if(validuser.isValidUser())
            {
                //request.setAttribute("validUser", validUser);
                //go to dashboard, which is either html or jsp
                if(validuser.getRoleId()==1)
                {
                    //request.getRequestDispatcher("adminIndex.jsp").forward(request, response);
                    return "ADMIN";
                }
                else
                {
                   // request.getRequestDispatcher("newjsp.jsp").forward(request, response);
                    return "CUSTOMER";
                }
            }
            else
                {
                   // request.getRequestDispatcher("newjsp.jsp").forward(request, response);
                    return "LOGIN";
                }
}*/
    public String registerUser() throws Exception {
        setCtr(getDao().addUser(getUserName(), getUserMobileNo(), getUserEmail(), getPassword(), getCity(), getCountry()));
        if (getCtr() > 0) {
            setMsg("Registration Successfull");
        } else {
            setMsg("Some error");
        }
        return "LOGIN";

    }

    /*
        setUserName(userName);
        setUserMobileNo(userMobileNo);
        setUserEmail(userEmail);
        setPassword(password);
        setCity(password);
        setCountry(city);
     */
 /*
public String reportUser(){
    try {
        
         
            Admin admin = new Admin();
            setUserList(new ArrayList<>());
            setUserList(admin.report());
            

            if (!userList.isEmpty() ) {
                setNoData(false);
                System.out.println("Users retrieve = "+getUserList().size());
                System.out.println("setting nodata=false");
            } else {
                setNoData(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "REPORT";
}

public String updateUser(){
     try {
            if (getSubmitType().equals("updatedata")) {
                User user = getDao().fetchUserDetails(getUserName().trim());
                if (user != null) {
                    setUserName(user.getUserName());
                    setPassword(user.getPassword());
                    setFirstName(user.getFirstName());
                    setLastName(user.getLastName());
                    setEmail(user.getEmail());
                    setPhoneNumber(user.getPhoneNumber());
                }
            } else {
                int i = getDao().updateUserDetails(getUserName(), getPassword(), getFirstName(), getLastName(), getEmail(), getPhoneNumber());
                if (i > 0) {
                    setMsg("Record Updated Successfuly");
                } else {
                    setMsg("error");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "UPDATE";
}

public String deleteUser(){
      try {
            int isDeleted = getDao().deleteUserDetails(getUserName());
            if (isDeleted > 0) {
                setMsg("Record deleted successfully");
            } else {
                setMsg("Some error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "DELETE";
}*/
    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the validUser
     */
    public boolean isValidUser() {
        return validUser;
    }

    /**
     * @param validUser the validUser to set
     */
    public void setValidUser(boolean validUser) {
        this.validUser = validUser;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @return the ctr
     */
    public int getCtr() {
        return ctr;
    }

    /**
     * @param ctr the ctr to set
     */
    public void setCtr(int ctr) {
        this.ctr = ctr;
    }

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }

    /**
     * @return the rs
     */
    public ResultSet getRs() {
        return rs;
    }

    /**
     * @param rs the rs to set
     */
    public void setRs(ResultSet rs) {
        this.rs = rs;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the userList
     */
    public List<User> getUserList() {
        return userList;
    }

    /**
     * @param userList the userList to set
     */
    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    /**
     * @return the noData
     */
    public boolean isNoData() {
        return noData;
    }

    /**
     * @param noData the noData to set
     */
    public void setNoData(boolean noData) {
        this.noData = noData;
    }

    /**
     * @return the submitType
     */
    public String getSubmitType() {
        return submitType;
    }

    /**
     * @param submitType the submitType to set
     */
    public void setSubmitType(String submitType) {
        this.submitType = submitType;
    }

    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return the userMobileNo
     */
    public String getUserMobileNo() {
        return userMobileNo;
    }

    /**
     * @param userMobileNo the userMobileNo to set
     */
    public void setUserMobileNo(String userMobileNo) {
        this.userMobileNo = userMobileNo;
    }

    /**
     * @return the userEmail
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * @param userEmail the userEmail to set
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the roleId
     */
    public int getRoleId() {
        return roleId;
    }

    /**
     * @param roleId the roleId to set
     */
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    /**
     * @return the userservice
     */
    public UserService getUserservice() {
        return userservice;
    }

    /**
     * @param userservice the userservice to set
     */
    public void setUserservice(UserService userservice) {
        this.userservice = userservice;
    }

    /**
     * @return the dao
     */
    public UserService getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(UserService dao) {
        this.dao = dao;
    }

    /**
     * @return the sessionMap
     */
    public SessionMap<String, Object> getSessionMap() {
        return sessionMap;
    }

    /**
     * @param sessionMap the sessionMap to set
     */
    public void setSessionMap(SessionMap<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }

   

    /*

    /**
     * @return the reportService
     */
    public ReportService getReportService() {
        return reportService;
    }

    /**
     * @param reportService the reportService to set
     */
    public void setReportService(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * @return the session
     */
    public boolean isSession() {
        return session;
    }

    /**
     * @param session the session to set
     */
    public void setSession(boolean session) {
        this.session = session;
    }

    /**
     * @return the us
     */
    public UserService getUs() {
        return us;
    }

    /**
     * @param us the us to set
     */
    public void setUs(UserService us) {
        this.us = us;
    }

    /**
     * @return the usd
     */
    public UserService getUsd() {
        return usd;
    }

    /**
     * @param usd the usd to set
     */
    public void setUsd(UserService usd) {
        this.usd = usd;
    }

    /**
     * @return the itemsList
     */
    public List<Integer> getItemsList() {
        return itemsList;
    }

    /**
     * @param itemsList the itemsList to set
     */
    public void setItemsList(List<Integer> itemsList) {
        this.itemsList = itemsList;
    }

}
