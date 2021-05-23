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
            desc:'',
            miaA:0,
            maxA:0,
            rType:'',
            ppm: 0,
            flag:1,
            buttonDisabled: false,
            isLoading: true
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
            desc:'',
            miaA:0,
            maxA:0,
            rType:'',
            ppm: 0,
            buttonDisabled: false,
            isLoading: false
        })
    }

    
    componentDidMount() {
        fetch(`${url}/cleaning-providers/services/${localStorage.getItem("serviceId")}`, {
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
            isLoading: false,
            name: result.name,
            desc: result.description,
            minA: result.minArea,
            maxA: result.maxArea,
            rType: result.placementType,
            ppm: result.pricePerMeter
        });
        },
        (error) => {
        this.setState({
            isLoading: false,
            error
        });
        }
        )
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
        if(!this.checkRType(this.state.rType)){
            return
        }
        if(!this.checkDesc(this.state.desc)){
            return
        }
        if(!this.checkminA(this.state.minA)){
            return
        }
        if(!this.checkmaxA(this.state.maxA)){
            return
        }
        if(!this.checkPrice(this.state.ppm)){
            return
        }

        this.setState({
            buttonDisabled: true,
            isLoading: true
        })

        this.editService()        
    }

    async editService() {
        try{
            let res = await fetch(`${url}/cleaning-providers/${decoded.email}/services`, {
                method: 'put',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('Token')
                },
                body: JSON.stringify({
                    id: localStorage.getItem("serviceId"),
                    description: this.state.desc,
                    maxArea: this.state.maxA,
                    minArea: this.state.minA,
                    name: this.state.name,
                    pricePerMeter: this.state.ppm,
                    placementType: this.state.rType
                })
            })
            let result = await res.json()
            if(result && result.id !== null){
                localStorage.removeItem("serviceId")
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
        return (
            <div
                className="w3-container w3-card-4 w3-light-grey w3-text-indigo w3-margin"
                style={{width: "700px", fontSize: "22px"}}>
                <h1 className="w3-center">{t('Edit')}</h1>
                <div className="sized-font w3-center w3-text-red">
                    {this.state.flag === 2 && <p>{t("EName")}</p>}
                    {this.state.flag === 3 && <p>{t("EMinA")}</p>}
                    {this.state.flag === 4 && <p>{t("EMaxA")}</p>}
                    {this.state.flag === 5 && <p>{t("EDesc")}</p>}
                    {this.state.flag === 6 && <p>{t("EType")}</p>}
                    {this.state.flag === 7 && <p>{t("EPPM")}</p>}
                    {this.state.flag === 10 && <p>{t("EError")}</p>}
                </div>
                <label>{t('DName')}</label>
                <Input
                    className={this.state.flag === 2 ? inputClass + " w3-border-red" : inputClass}
                    type='text'
                    value={this.state.name ? this.state.name : ''}
                    onChange={(val) => this.setInputValue('name', val)}
                />
                <label>{t('Type')}</label>
                <Input
                    className={this.state.flag === 6 ? inputClass + " w3-border-red" : inputClass}
                    type='text'
                    value={this.state.rType ? this.state.rType : ''}
                    onChange={(val) => this.setInputValue('rType', val)}
                />
                <label>{t('Desc')}</label>
                <Input
                    className={this.state.flag === 5 ? inputClass + " w3-border-red" : inputClass}
                    type='text'
                    value={this.state.desc ? this.state.desc : ''}
                    onChange={(val) => this.setInputValue('desc', val)}
                />
                <label>{t('minA')}</label>
                <Input
                    className={this.state.flag === 3 ? inputClass + " w3-border-red" : inputClass}
                    type='text'
                    value={this.state.minA ? this.state.minA : ''}
                    onChange={(val) => this.setInputValue('minA', val)}
                />
                <label>{t('maxA')}</label>
                <Input
                    className={this.state.flag === 4 ? inputClass + " w3-border-red" : inputClass}
                    type='text'
                    value={this.state.maxA ? this.state.maxA : ''}
                    onChange={(val) => this.setInputValue('maxA', val)}
                />
                <label>{t('PPM')}</label>
                <Input
                    className={this.state.flag === 7 ? inputClass + " w3-border-red" : inputClass}
                    type='text'
                    value={this.state.ppm ? this.state.ppm : ''}
                    onChange={(val) => this.setInputValue('ppm', val)}
                />
                <Button
                    className="w3-btn w3-block w3-section w3-indigo w3-padding"
                    text={t('Save')}
                    disabled={this.state.buttonDisabled}
                    onClick={() => this.checkCred()}
                />
            </div>
        )
    }
}

export default withTranslation() (EditForm);
