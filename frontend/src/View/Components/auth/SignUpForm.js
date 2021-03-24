import React from 'react'
import Input from '../Input'
import Button from '../Button'
import { withTranslation } from 'react-i18next'

var url = "http://localhost:8080";

class SignUpForm extends React.Component{
    constructor(props) {
        super(props)
        this.state = {
            name: '',
            email:'',
            password:'',
            phone:'',
            city:'',
            country:'',
            street:'',
            house:'',
            flag: 1,
            lat:0.0,
            lng:0.0,
            buttonDisabled: false
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
            phone:'',
            city:'',
            country:'',
            street:'',
            house:'',
            buttonDisabled: false
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
    checkPass(password) {
        if(password.length < 8){
            this.setState({flag: 3}); 
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

    checkCred(){
        if(!this.checkName(this.state.name)){
            return
        }
        if(!this.checkEmail(this.state.email)){
            return
        }
        if(!this.checkPass(this.state.password)){
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

        this.setState({
            buttonDisabled: true
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
            if(result && result.id !== null){
                window.location.href='./login';
            } else if (result){
                this.resetForm()
                this.setState({flag: 9}); 
            }
        }
        catch(e){
            console.log(e)
            this.resetForm()
        }
    }

    render() {
        const {t} = this.props
        return(
            <div className="signUpForm">
                <div className='signUpContainer'>
                    <h1>{t('Signup')}</h1>
                    { this.state.flag === 2 && <p>{t("EEmail")}</p>}
                    { this.state.flag === 3 && <p>{t("EPass")}</p>}
                    { this.state.flag === 4 && <p>{t("EName")}</p>}
                    { this.state.flag === 5 && <p>{t("EPhone")}</p>}
                    { this.state.flag === 6 && <p>{t("ECountry")}</p>}
                    { this.state.flag === 7 && <p>{t("ECity")}</p>}
                    { this.state.flag === 8 && <p>{t("EStreet")}</p>}
                    { this.state.flag === 9 && <p>{t("EHouse")}</p>}
                    { this.state.flag === 10 && <p>{t("eExist")}</p>}
                    <Input
                        type = 'text'
                        placeholder = {t('DName')}
                        value={this.state.name ? this.state.name : ''}
                        onChange = { (val) => this.setInputValue('name', val)}
                    />
                    { this.state.flag === 2 && <p>Your login credentials could not be verified, please try again.</p>}
                    <Input
                        type = 'text'
                        placeholder = {t('Email')}
                        value={this.state.email ? this.state.email : ''}
                        onChange = { (val) => this.setInputValue('email', val)}
                    />
                     <Input
                        type = 'text'
                        placeholder = {t('Phone')}
                        value={this.state.phone ? this.state.phone : ''}
                        onChange = { (val) => this.setInputValue('phone', val)}
                    />
                    <Input
                        type = 'text'
                        placeholder = {t('FCountry')}
                        value={this.state.country ? this.state.country : ''}
                        onChange = { (val) => this.setInputValue('country', val)}
                    />
                    <Input
                        type = 'text'
                        placeholder = {t('FCity')}
                        value={this.state.city ? this.state.city : ''}
                        onChange = { (val) => this.setInputValue('city', val)}
                    />
                    <Input
                        type = 'text'
                        placeholder = {t('FStreet')}
                        value={this.state.street ? this.state.street : ''}
                        onChange = { (val) => this.setInputValue('street', val)}
                    />
                    <Input
                        type = 'text'
                        placeholder = {t('FHouse')}
                        value={this.state.house ? this.state.house : ''}
                        onChange = { (val) => this.setInputValue('house', val)}
                    />
                    <Input
                        type = 'password'
                        placeholder = {t('Password')}
                        value={this.state.password ? this.state.password : ''}
                        onChange = { (val) => this.setInputValue('password', val)}
                    />
                    <Button
                        text = {t('Signup')}
                        disabled = {this.state.buttonDisabled}
                        onClick = { () => this.checkCred()}
                    />
                </div>
            </div>
        )
    }
}

export default withTranslation() (SignUpForm);
