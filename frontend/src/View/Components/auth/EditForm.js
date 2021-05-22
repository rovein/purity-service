import React from 'react'
import Input from '../ui/Input'
import Button from '../ui/Button'
import { withTranslation } from 'react-i18next'
import jwt_decode from "jwt-decode"
import * as Constants from "../util/Constants";
import Loader from "react-loader-spinner";

var url = "http://localhost:8080";
if(localStorage.getItem("Token") != null){
    var token = localStorage.getItem("Token")
    var decoded = jwt_decode(token)
}

class EditForm extends React.Component{
    constructor(props) {
        super(props)
        this.state = {
            name: '',
            address:{},
            email:'',
            phone:'',
            id:'',
            password:'',
            confirmPassword: '',
            flag:1,
            buttonDisabled: false,
            isLoaded: false
        }
    }

    setInputValue(property, val) {
        this.setState({
            [property]: val
        })
    }

    setAddressValue(property, val) {
        let updatedAddress = this.state.address
        updatedAddress[property] = val
        this.setState({address: updatedAddress})
    }

    resetForm(){
        this.setState({
            name: '',
            email: '',
            phone:'',
            city:'',
            country:'',
            password:'',
            confirmPassword: '',
            street:'',
            house:'',
            buttonDisabled: false,
            isLoaded: true
        })
    }

    
    componentDidMount() {
        if(decoded.role === "PLACEMENT_OWNER"){
            this.getData(`${url}/placement-owners/${decoded.email}`);
        }else if(decoded.role === "CLEANING_PROVIDER"){
            this.getData(`${url}/cleaning-providers/${decoded.email}`);
        }else if(decoded.role === "ADMIN"){
            if(localStorage.getItem("Role") === "PLACEMENT_OWNER"){
                this.getData(`${url}/placement-owners/${localStorage.getItem("Email")}`);
            }else if(localStorage.getItem("Role") === "CLEANING_PROVIDER"){
                this.getData(`${url}/cleaning-providers/${localStorage.getItem("Email")}`);
            }
        }
    }

      getData(resUrl){
        fetch(resUrl, {
            method: 'get',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        })
        .then(res => res.json())
        .then(
        (result) => {
        this.setState({
            isLoaded: true,
            name: result.name,
            email: result.email,
            phone: result.phoneNumber,
            address: result.address,
            id: result.id,
            company: result
        });
        },
        (error) => {
        this.setState({
            isLoaded: true,
            error
        });
        })
      }

      checkEmail(email) {
        let regEmail = new RegExp('^([a-z0-9_-]+.)*[a-z0-9_-]+@[a-z0-9_-]+(.[a-z0-9_-]+)*.[a-z]{2,6}$');
        if(!regEmail.test(email)){
            this.setState({flag: 2});
          return false
        }
        return true
    }

    checkName(name) {
        let regName = new RegExp('^([А-ЯЁа-яё0-9]+)|([A-Za-z0-9]+)$');
        if(!regName.test(name)){
            this.setState({flag: 4}); 
            return false
        }
        return true
    }

    checkPhone(phone) {
        let regPhone = new RegExp('^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-s./0-9]*$');
        if(!regPhone.test(phone)){
            this.setState({flag: 5}); 
            return false
        }
        return true
    }

    checkCountry(country) {
        let regCountry = new RegExp('^([А-Яа-яё]+)|([a-z]+)$');
        if(!regCountry.test(country)){
            this.setState({flag: 6}); 
            return false
        }
        return true
    }

    checkCity(city) {
        let regCity = new RegExp('^([А-Яа-яё]+)|([a-z]+)$');
        if(!regCity.test(city)){
            this.setState({flag: 7}); 
            return false
        }
        return true
    }

    checkStreet(street) {
        let regStreet = new RegExp('^([А-Яа-яё]+)|([a-z]+)$');
        if(!regStreet.test(street)){
            this.setState({flag: 8}); 
            return false
        }
        return true
    }

    checkHouseNum(house) {
        let regHouse = new RegExp('^([0-9A-Za-zА-яа-яёЁ]+)$');
        if(!regHouse.test(house)){
            this.setState({flag: 9}); 
            return false
        }
        return true
    }

    checkPass(password) {
        if(password.length < 8){
            this.setState({flag: 11});
            return false
        }
        return true
    }

    checkPasswords(password, confirmPassword) {
        if (password !== confirmPassword) {
            this.setState({flag: 12});
            return false
        }
        return true
    }

    checkCred(){
        if(!this.checkName(this.state.name)){
            return
        }
        if(!this.checkEmail(this.state.email)){
            return
        }
        if(!this.checkPhone(this.state.phone)){
            return
        }
        if(!this.checkCountry(this.state.country)){
            return
        }
        if(!this.checkCity(this.state.city)){
            return
        }
        if(!this.checkStreet(this.state.street)){
            return
        }
        if(!this.checkHouseNum(this.state.house)){
            return
        }
        if(this.state.password && !this.checkPass(this.state.password)){
            return
        }
        if (this.state.confirmPassword && !this.checkPasswords(this.state.password, this.state.confirmPass)) {
            return
        }

        this.setState({
            buttonDisabled: true,
            isLoaded: false
        })

        if(decoded.role === "PLACEMENT_OWNER"){
            this.editCompany(`${url}/placement-owners`);
        }else if(decoded.role === "CLEANING_PROVIDER"){
            this.editCompany(`${url}/cleaning-providers`);
        }else if(decoded.role === "ADMIN"){
            if(localStorage.getItem("Role") === "PLACEMENT_OWNER"){
                this.editCompany(`${url}/placement-owners`);
            }else if(localStorage.getItem("Role") === "CLEANING_PROVIDER"){
                this.editCompany(`${url}/cleaning-providers`);
            }
        }
    }

    async editCompany(resUrl) {
        try{
            var role;
            if(decoded.role !== "ADMIN"){
                role = decoded.role
            }else{
                role = localStorage.getItem("Role")
            }

            let requestBody = {
                address: {
                    city: this.state.address.city,
                    country: this.state.address.country,
                    houseNumber: this.state.address.houseNumber,
                    latitude: this.state.address.latitude,
                    longitude: this.state.address.longitude,
                    street: this.state.address.street
                },
                name: this.state.name,
                email: this.state.email,
                id: this.state.id,
                phoneNumber: this.state.phone,
                role: role
            }

            if (this.state.password) {
                requestBody.password = this.state.password;
            }

            let res = await fetch(resUrl, {
                method: 'put',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('Token')
                },
                body: JSON.stringify(requestBody)
            })
            let result = await res.json()
            if (result && result.id !== null){
                localStorage.setItem("placementOwner", JSON.stringify({
                    name: this.state.name,
                    email: this.state.email,
                    id: this.state.id,
                    phoneNumber: this.state.phone,
                    role: role
                }));
                localStorage.setItem("placementOwnerAddress", JSON.stringify(requestBody.address));
                window.location.href='./profile';
            } else if (result){
                this.resetForm()
                this.setState({flag: 10}); 
            }
        }
        catch(e){
            console.log(e)
            this.resetForm()
        }
    }

    render() {
        const {t} = this.props
        const inputClass = Constants.INPUT_STYLE_CLASSES;
        if (!this.state.isLoaded) {
            return <div>
                <Loader
                  type="Oval" //Audio Oval ThreeDots
                  color="#4B0082"
                  height={425}
                  width={425}
                  timeout={10000}
                />
            </div>
        }
        return(
          <div
            className="w3-container w3-card-4 w3-light-grey w3-text-indigo w3-margin"
            style={{width: "700px"}}>
              <h1 className="w3-center">{t('Edit')}</h1>
              <div className="sized-font w3-center w3-text-red">
                  {this.state.flag === 2 && <span>{t("EEmail")}</span>}
                  {this.state.flag === 4 && <p>{t("EName")}</p>}
                  {this.state.flag === 5 && <p>{t("EPhone")}</p>}
                  {this.state.flag === 6 && <p>{t("ECountry")}</p>}
                  {this.state.flag === 7 && <p>{t("ECity")}</p>}
                  {this.state.flag === 8 && <p>{t("EStreet")}</p>}
                  {this.state.flag === 9 && <p>{t("EHouse")}</p>}
                  {this.state.flag === 10 && <p>{t("eExist")}</p>}
                  {this.state.flag === 11 && <p>{t("EPass")}</p>}
                  { this.state.flag === 12 && <p>{t("EConfirmPass")}</p>}
              </div>
              <div className="w3-row w3-section">
                  <div className="w3-col" style={{width: "50px"}}>
                      <i className="w3-xxlarge fa fa-user"/>
                  </div>
                  <div className="w3-rest">
                      <Input
                        className={this.state.flag === 4 ? inputClass + " w3-border-red" : inputClass}
                        type='text'
                        placeholder={t('DName')}
                        value={this.state.name ? this.state.name : ''}
                        onChange={(val) => this.setInputValue('name', val)}
                      />
                  </div>
              </div>
              <div className="w3-row w3-section">
                  <div className="w3-col" style={{width: "50px"}}>
                      <i className="w3-xxlarge fas fa-envelope"/>
                  </div>
                  <div className="w3-rest">
                      <Input
                        disabled="true"
                        className={this.state.flag === 2 ? inputClass + " w3-border-red" : inputClass}
                        type='text'
                        placeholder={t('Email')}
                        value={this.state.email ? this.state.email : ''}
                        onChange={(val) => this.setInputValue('email', val)}
                      />
                  </div>
              </div>
              <div className="w3-row w3-section">
                  <div className="w3-col" style={{width: "50px"}}>
                      <i className="w3-xxlarge fas fa-phone-alt"/>
                  </div>
                  <div className="w3-rest">
                      <Input
                        className={this.state.flag === 5 ? inputClass + " w3-border-red" : inputClass}
                        type='text'
                        placeholder={t('Phone')}
                        value={this.state.phone ? this.state.phone : ''}
                        onChange={(val) => this.setInputValue('phone', val)}
                      />
                  </div>
              </div>
              <div className="w3-row w3-section">
                  <div className="w3-col" style={{width: "50px"}}>
                      <i className="w3-xxlarge fas fa-flag"/>
                  </div>
                  <div className="w3-rest">
                      <Input
                        className={this.state.flag === 6 ? inputClass + " w3-border-red" : inputClass}
                        type='text'
                        placeholder={t('FCountry')}
                        value={this.state.address.country ? this.state.address.country : ''}
                        onChange={(val) => this.setAddressValue('country', val)}
                      />
                  </div>
              </div>
              <div className="w3-row w3-section">
                  <div className="w3-col" style={{width: "50px"}}>
                      <i className="w3-xxlarge fas fa-city"/>
                  </div>
                  <div className="w3-rest">
                      <Input
                        className={this.state.flag === 7 ? inputClass + " w3-border-red" : inputClass}
                        type='text'
                        placeholder={t('FCity')}
                        value={this.state.address.city ? this.state.address.city : ''}
                        onChange={(val) => this.setAddressValue('city', val)}
                      />
                  </div>
              </div>
              <div className="w3-row w3-section">
                  <div className="w3-col" style={{width: "50px"}}>
                      <i className="w3-xxlarge fas fa-road"/>
                  </div>
                  <div className="w3-rest">
                      <Input
                        className={this.state.flag === 8 ? inputClass + " w3-border-red" : inputClass}
                        type='text'
                        placeholder={t('FStreet')}
                        value={this.state.address.street ? this.state.address.street : ''}
                        onChange={(val) => this.setAddressValue('street', val)}
                      />
                  </div>
              </div>
              <div className="w3-row w3-section">
                  <div className="w3-col" style={{width: "50px"}}>
                      <i className="w3-xxlarge fas fa-home"/>
                  </div>
                  <div className="w3-rest">
                      <Input
                        className={this.state.flag === 9 ? inputClass + " w3-border-red" : inputClass}
                        type='text'
                        placeholder={t('FHouse')}
                        value={this.state.address.houseNumber ? this.state.address.houseNumber : ''}
                        onChange={(val) => this.setAddressValue('houseNumber', val)}
                      />
                  </div>
              </div>
              <h4 className="w3-center">{t("PasswordChangeMsg")}</h4>
              <div className="w3-row w3-section">
                  <div className="w3-col" style={{width: "50px"}}>
                      <i className="w3-xxlarge fas fa-lock"/>
                  </div>
                  <div className="w3-rest">
                      <Input
                        className={(this.state.flag === 11 || this.state.flag === 12) ? inputClass + " w3-border-red" : inputClass}
                        type='password'
                        placeholder={t('Password')}
                        value={this.state.password ? this.state.password : ''}
                        onChange={(val) => this.setInputValue('password', val)}
                      />
                  </div>
              </div>
              <div className="w3-row w3-section">
                  <div className="w3-col" style={{width: "50px"}}>
                      <i className="w3-xxlarge fas fa-lock"/>
                  </div>
                  <div className="w3-rest">
                      <Input
                        className={this.state.flag === 12 ? inputClass + " w3-border-red" : inputClass}
                        type = 'password'
                        placeholder = {t('ConfirmPassword')}
                        value={this.state.confirmPass ? this.state.confirmPass : ''}
                        onChange = { (val) => this.setInputValue('confirmPass', val)}
                      />
                  </div>
              </div>
              <Button
                className="w3-btn w3-block w3-section w3-indigo w3-padding"
                text={t('Edit')}
                disabled={this.state.buttonDisabled}
                onClick={() => this.checkCred()}
              />
          </div>
        )
    }
}

export default withTranslation() (EditForm);
