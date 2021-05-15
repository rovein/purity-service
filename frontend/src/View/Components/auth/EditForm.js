import React from 'react'
import Input from '../ui/Input'
import Button from '../ui/Button'
import { withTranslation } from 'react-i18next'
import jwt_decode from "jwt-decode"

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
            flag:1,
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
            phone:'',
            city:'',
            country:'',
            password:'',
            street:'',
            house:'',
            buttonDisabled: false
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
            // country: result.address.country,
            // city: result.address.city,
            // street: result.address.street,
            // houseNumber: result.address.houseNumber,
            id: result.id,
            company: result
        });
        },
        (error) => {
        this.setState({
            isLoaded: true,
            error
        });
        }
        )
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
        let regHouse = new RegExp('^([0-9A-Za-zА-яа-яёЁ]+)$');
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
            var role
            if(decoded.role !== "ADMIN"){
                role = decoded.role
            }else{
                role = localStorage.getItem("Role")
            }
            let res = await fetch(resUrl, {
                method: 'put',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('Token')
                },
                body: JSON.stringify({
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
                    password: this.state.password,
                    role: role
                })
            })
            let result = await res.json()
            if(result && result.id !== null){
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
        return(
            <div className="signUpForm">
                <div className='signUpContainer'>
                    <h1>{t('Edit')}</h1>
                    { this.state.flag === 2 && <p>{t("EEmail")}</p>}
                    { this.state.flag === 3 && <p>{t("EPass")}</p>}
                    { this.state.flag === 4 && <p>{t("EName")}</p>}
                    { this.state.flag === 5 && <p>{t("EPhone")}</p>}
                    { this.state.flag === 6 && <p>{t("ECountry")}</p>}
                    { this.state.flag === 7 && <p>{t("ECity")}</p>}
                    { this.state.flag === 8 && <p>{t("EStreet")}</p>}
                    { this.state.flag === 9 && <p>{t("EHouse")}</p>}
                    { this.state.flag === 10 && <p>{t("EError")}</p>}
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
                        type = 'password'
                        placeholder = {t('Password')}
                        value={this.state.password ? this.state.password : ''}
                        onChange = { (val) => this.setInputValue('password', val)}
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
                        value={this.state.address.country ? this.state.address.country : ''}
                        onChange = { (val) => this.setInputValue('country', val)}
                    />
                    <Input
                        type = 'text'
                        placeholder = {t('FCity')}
                        value={this.state.address.city ? this.state.address.city : ''}
                        onChange = { (val) => this.setInputValue('city', val)}
                    />
                    <Input
                        type = 'text'
                        placeholder = {t('FStreet')}
                        value={this.state.address.street ? this.state.address.street : ''}
                        onChange = { (val) => this.setInputValue('street', val)}
                    />
                    <Input
                        type = 'text'
                        placeholder = {t('FHouse')}
                        value={this.state.address.houseNumber ? this.state.address.houseNumber : ''}
                        onChange = { (val) => this.setInputValue('house', val)}
                    />
                    <Button
                        text = {t('Save')}
                        disabled = {this.state.buttonDisabled}
                        onClick = { () => this.checkCred()}
                    />
                </div>
            </div>
        )
    }
}

export default withTranslation() (EditForm);
