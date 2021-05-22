import React from 'react'
import Input from '../ui/Input'
import Button from '../ui/Button'
import { withTranslation } from 'react-i18next'
import Loader from "react-loader-spinner";

var url = "http://localhost:8080";

class SignUpForm extends React.Component{
    constructor(props) {
        super(props)
        this.state = {
            name: '',
            email:'',
            password:'',
            confirmPass: '',
            phone:'',
            city:'',
            country:'',
            street:'',
            house:'',
            flag: 1,
            lat:0.0,
            lng:0.0,
            buttonDisabled: false,
            isLoading: false
        }
    }

    setInputValue(property, val) {
        this.setState({
            [property]: val
        })
    }

    resetForm(){
        this.setState({
            name: '',
            email: '',
            password: '',
            confirmPass: '',
            phone:'',
            city:'',
            country:'',
            street:'',
            house:'',
            buttonDisabled: false,
            isLoading: false
        })
    }

    redirectRes() {
        if(this.props.role === "PLACEMENT_OWNER"){
            this.doSignUp(`${url}/auth/register/placement-owner`);
        }else if(this.props.role === "CLEANING_PROVIDER"){
            this.doSignUp(`${url}/auth/register/cleaning-provider`);
        }
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
        let regHouse = new RegExp('^([0-9]+)$');
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
        if(!this.checkPass(this.state.password)){
            return
        }
        if (!this.checkPasswords(this.state.password, this.state.confirmPass)) {
            return
        }

        this.setState({
            buttonDisabled: true,
            isLoading: true
        })

        this.redirectRes()
    }

    async doSignUp(resUrl) {
        try{
            let res = await fetch(resUrl, {
                method: 'post',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    address: {
                        city: this.state.city,
                        country: this.state.country,
                        houseNumber: this.state.house,
                        latitude: this.state.lat,
                        longitude: this.state.lng,
                        street: this.state.street
                    },
                    name: this.state.name,
                    email: this.state.email,
                    creationDate:new Date(),
                    phoneNumber: this.state.phone,
                    role: this.props.role,
                    password: this.state.password
                })
            })
            let result = await res.json()
            if (res.status === 400 && result.email) {
                this.setState({flag: 10, buttonDisabled: false, isLoading: false});
            } else if(result && result.id !== null) {
                window.location.href='./login';
            } else if (result){
                this.resetForm()
            }
        }
        catch(e){
            console.log(e)
            this.resetForm()
        }
    }

    render() {
        const {t} = this.props
        const inputClass = "w3-input w3-border w3-hover-sand";
        if (this.state.isLoading) {
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
              <h1 className="w3-center">{t('Signup')}</h1>
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
                        value={this.state.country ? this.state.country : ''}
                        onChange={(val) => this.setInputValue('country', val)}
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
                        value={this.state.city ? this.state.city : ''}
                        onChange={(val) => this.setInputValue('city', val)}
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
                        value={this.state.street ? this.state.street : ''}
                        onChange={(val) => this.setInputValue('street', val)}
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
                        value={this.state.house ? this.state.house : ''}
                        onChange={(val) => this.setInputValue('house', val)}
                      />
                  </div>
              </div>
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
                text={t('Signup')}
                disabled={this.state.buttonDisabled}
                onClick={() => this.checkCred()}
              />
          </div>
        )
    }
}

export default withTranslation() (SignUpForm);
