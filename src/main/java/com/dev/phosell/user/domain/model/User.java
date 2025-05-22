package com.dev.phosell.user.domain.model;

import com.dev.phosell.user.domain.exception.InvalidUserValueException;
import java.util.UUID;
import java.util.regex.Pattern;

public class User {

    private UUID id;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String city;
    private String address;
    private String curp;
    private String idPhotoFront;
    private String idPhotoBack;
    private Role role;
    private Boolean isInService;


    public User(){
        this.id = UUID.randomUUID();
    }

    public User(UUID id, String fullName, String email, String password, String phone, String city, String address, String curp, String idPhotoFront, String idPhotoBack, Role role, Boolean isInService) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.city = city;
        this.address = address;
        this.curp = curp;
        this.idPhotoFront = idPhotoFront;
        this.idPhotoBack = idPhotoBack;
        this.role = role;
        this.isInService = isInService;
    }

    public void validateClient(){
        validateId();
        validateFullName();
        validateEmail();
        validatePassword();
        validatePhone();
        validateCity();
        validateRole();

        if(this.role != Role.CLIENT){
            throw new InvalidUserValueException("role",this.role.toString(),"The user client has a different role.");
        }
    }

    public void validatePhotographer(){
        validateId();
        validateFullName();
        validateEmail();
        validatePassword();
        validatePhone();
        validateAddress();
        validateCurp();
        validateIdPhotoFront();
        validateIdPhotoBack();
        validateIsInService();
        validateRole();

        if(this.role != Role.PHOTOGRAPHER){
            throw new InvalidUserValueException("role",this.role.toString(),"The user photographer has a different role.");
        }
    }

    private void validateId(){
        if(this.id == null){
            throw  new InvalidUserValueException("id",null);
        }
    }
    private void validateFullName(){
        if (this.fullName == null){
            throw new InvalidUserValueException("fullName",null);
        }
    }
    private  void validateEmail(){
        if(this.email == null){
            throw  new InvalidUserValueException("email",null);
        }
        if (!EMAIL_PATTERN.matcher(this.email).matches()) {
            throw new InvalidUserValueException("email", this.email);
        }
    }
    private void validatePassword(){
        if (this.password == null){
            throw new InvalidUserValueException("password",null);
        }
    }
    private void validatePhone(){
        if(this.phone == null){
            throw new InvalidUserValueException("phone",null);
        }
    }
    private void validateCity(){
        if(this.city == null){
            throw new InvalidUserValueException("city",null);
        }
    }
    private void validateAddress(){
        if (this.address == null){
            throw new InvalidUserValueException("address",null);
        }
    }
    private void validateCurp(){
        if(this.curp == null){
            throw new InvalidUserValueException("curp",null);
        }
    }
    private void  validateIdPhotoFront(){
        if (this.idPhotoFront == null){
            throw  new InvalidUserValueException("idPhotoFront",null);
        }
    }
    private void  validateIdPhotoBack(){
        if (this.idPhotoBack == null){
            throw  new InvalidUserValueException("idPhotoBack",null);
        }
    }
    private void validateIsInService(){
        if(this.isInService == null){
            throw new InvalidUserValueException("isInService",null);
        }
    }
    private void validateRole(){
        if(this.role == null){
            throw new InvalidUserValueException("role",null);
        }
    }

    // regular expressions
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE
    );



    // setter
    public void setId(UUID id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public void setIdPhotoFront(String idPhotoFront) {
        this.idPhotoFront = idPhotoFront;
    }

    public void setIdPhotoBack(String idPhotoBack) {
        this.idPhotoBack = idPhotoBack;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setInService(Boolean inService) {
        isInService = inService;
    }

    // getter

    public UUID getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getCurp() {
        return curp;
    }

    public String getIdPhotoFront() {
        return idPhotoFront;
    }

    public String getIdPhotoBack() {
        return idPhotoBack;
    }

    public Role getRole() {
        return role;
    }

    public Boolean getInService() {
        return isInService;
    }
}
