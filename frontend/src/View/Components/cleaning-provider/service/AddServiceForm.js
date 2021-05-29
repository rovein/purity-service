import React from 'react'
import Input from '../../ui/Input'
import Button from '../../ui/Button'
import { withTranslation } from 'react-i18next'
import jwt_decode from "jwt-decode"
import * as Constants from "../../util/Constants";
import Loader from "react-loader-spinner";

var url = "http://localhost:8080";
if(localStorage.getItem("Token") != null){
    var token = localStorage.getItem("Token")
    var decoded = jwt_decode(token)
}

class AddServiceForm extends React.Component{
    constructor(props) {
        super(props)
        this.state = {
            type: '',
            desc: '',
            maxAr: 0,
            minAr: 0,
            name: '',
            ppm: 0,
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
            type: '',
            desc: '',
            maxAr: 0,
            minAr: 0,
            name: '',
            ppm: 0,
            buttonDisabled: false,
            isLoading: false
        })
    }


    checkName(name) {
        let editName = new RegExp('^([А-ЯЁа-яё0-9]+)|([A-Za-z0-9]+)$');
        if(!editName.test(name)){
            this.setState({flag: 2}); 
            return false
        }
        return true
    }

    checkminA(minA) {
        let editMinA = new RegExp('^([0-9]+)$');
        if(!editMinA.test(minA) || !minA){
            this.setState({flag: 3}); 
            return false
        }
        return true
    }
    checkmaxA(maxA) {
        let editMaxA = new RegExp('^([0-9]+)$');
        if(!editMaxA.test(maxA) || !maxA){
            this.setState({flag: 4}); 
            return false
        }
        return true
    }

    checkDesc(desc) {
        if(desc.length < 1){
            this.setState({flag: 5}); 
            return false
        }
        return true
    }

    checkRType(rType) {
        let editType = new RegExp('^([А-Яа-яё]+)|([a-z]+)$');
        if(!editType.test(rType)){
            this.setState({flag: 6}); 
            return false
        }
        return true
    }

    checkPrice(price) {
        let editPrice = new RegExp('^([0-9]+)$');
        if(!editPrice.test(price) || !price){
            this.setState({flag: 7}); 
            return false
        }
        return true
    }

    checkCred(){
        if(!this.checkName(this.state.name)){
            return
        }
        if(!this.checkRType(this.state.type)){
            return
        }
        if(!this.checkDesc(this.state.desc)){
            return
        }
        if(!this.checkminA(this.state.minAr)){
            return
        }
        if(!this.checkmaxA(this.state.maxAr)){
            return
        }
        if(!this.checkPrice(this.state.ppm)){
            return
        }
        this.setState({
            buttonDisabled: true,
            isLoading: true
        })

        this.addService();      
    }

    async addService() {
        try{
            let res = await fetch(`${url}/cleaning-providers/${decoded.email}/services`, {
                method: 'post',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('Token')
                },
                body: JSON.stringify({
                    description: this.state.desc,
                    maxArea: this.state.maxAr,
                    minArea: this.state.minAr,
                    name: this.state.name,
                    pricePerMeter: this.state.ppm,
                    placementType: this.state.type
                })
            })
            let result = await res.json()
            if(result){
                window.location.href='./profile';
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
        if (this.state.isLoading) {
            return <div>
                <Loader
                    type="Oval" //Audio Oval ThreeDots
                    color="#4B0082"
                    height={400}
                    width={425}
                    timeout={10000}
                />
            </div>
        }
        return(
            <div
                className="w3-container w3-card-4 w3-light-grey w3-text-indigo w3-margin"
                style={{width: "700px", fontSize: "22px"}}>
                <h1 className="w3-center">{t('AddS')}</h1>
                <div className="sized-font w3-center w3-text-red">
                    { this.state.flag === 2 && <p>{t("EName")}</p>}
                    { this.state.flag === 3 && <p>{t("EMinA")}</p>}
                    { this.state.flag === 4 && <p>{t("EMaxA")}</p>}
                    { this.state.flag === 5 && <p>{t("EDesc")}</p>}
                    { this.state.flag === 6 && <p>{t("EType")}</p>}
                    { this.state.flag === 7 && <p>{t("EPPM")}</p>}
                    { this.state.flag === 10 && <p>{t("EError")}</p>}
                </div>
                    <label>{t('DName')}</label>
                    <Input
                        className={this.state.flag === 2 ? inputClass + " w3-border-red" : inputClass}
                        type = 'text'
                        value={this.state.name ? this.state.name : ''}
                        onChange = { (val) => this.setInputValue('name', val)}
                    />
                    <label>{t('Type')}</label>
                    <Input
                        className={this.state.flag === 6 ? inputClass + " w3-border-red" : inputClass}
                        type = 'text'
                        value={this.state.type ? this.state.type : ''}
                        onChange = { (val) => this.setInputValue('type', val)}
                    />
                    <label>{t('Desc')}</label>
                    <Input
                         className={this.state.flag === 5 ? inputClass + " w3-border-red" : inputClass}
                        type = 'text'
                        value={this.state.desc ? this.state.desc : ''}
                        onChange = { (val) => this.setInputValue('desc', val)}
                    />
                    <label>{t('minA')}</label>
                    <Input
                        className={this.state.flag === 3 ? inputClass + " w3-border-red" : inputClass}
                        type = 'text'
                        value={this.state.minAr ? this.state.minAr : ''}
                        onChange = { (val) => this.setInputValue('minAr', val)}
                    />
                    <label>{t('maxA')}</label>
                    <Input
                        className={this.state.flag === 4 ? inputClass + " w3-border-red" : inputClass}
                        type = 'text'
                        value={this.state.maxAr ? this.state.maxAr : ''}
                        onChange = { (val) => this.setInputValue('maxAr', val)}
                    />
                    <label>{t('PPM')}</label>
                    <Input
                        className={this.state.flag === 7 ? inputClass + " w3-border-red" : inputClass}
                        type = 'text'
                        value={this.state.ppm ? this.state.ppm : ''}
                        onChange = { (val) => this.setInputValue('ppm', val)}
                    />
                    <Button
                        className="w3-btn w3-block w3-section w3-indigo w3-padding"
                        text = {t('Add')}
                        disabled = {this.state.buttonDisabled}
                        onClick = { () => this.checkCred()}
                    />
            </div>
        )
    }
}

export default withTranslation() (AddServiceForm);