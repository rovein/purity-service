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

class AddRForm extends React.Component{
    constructor(props) {
        super(props)
        this.state = {
            type: '',
            area:'',
            floor: '',
            winCount:'',
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
            type: '',
            area:'',
            floor: '',
            winCount:'',
            buttonDisabled: false
        })
    }


    checkType(type) {
        let rType = new RegExp('^([А-Яа-яё]+)|([a-z]+)$');
        if(!rType.test(type)){
            this.setState({flag: 2}); 
          return false
        }
        return true
    }

    checkFloor(floor) {
        let rFloor = new RegExp('^([0-9]+)$');
        if(!rFloor.test(floor)){
            this.setState({flag: 3}); 
        return false
        }
        return true
    }

    checkArea(area) {
        let rArea = new RegExp('^([0-9]+)$');
        if(!rArea.test(area)){
            this.setState({flag: 4}); 
            return false
        }
        return true
    }

    checkWCount(winCount) {
        let rWCount = new RegExp('^([0-9]+)$');
        if(!rWCount.test(winCount)){
            this.setState({flag: 5}); 
            return false
        }
        return true
    }

    checkCred(){
        if(!this.checkType(this.state.type)){
            return
        }
        if(!this.checkFloor(this.state.floor)){
            return
        }
        if(!this.checkWCount(this.state.winCount)){
            return
        }
        if(!this.checkArea(this.state.area)){
            return
        }
        this.setState({
            buttonDisabled: true
        })

        this.addRoom();      
    }

    async addRoom() {
        try{
            let res = await fetch(`${url}/placement-owners/${decoded.email}/placements`, {
                method: 'post',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('Token')
                },
                body: JSON.stringify({
                    placementType: this.state.type,
                    area: this.state.area,
                    floor: this.state.floor,
                    windowsCount: this.state.winCount,
                    lastCleaning: new Date(),
                    smartDevice: {
                        adjustmentFactor: 0,
                        airQuality: 0,
                        humidity: 0,
                        temperature: 0,
                        dirtinessFactor: 0,
                        priority: "null"
                      },
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
        return(
            <div className="signUpForm">
                <div className='signUpContainer'>
                    <h1>{t('AddR')}</h1>
                    { this.state.flag === 2 && <p>{t("EType")}</p>}
                    { this.state.flag === 3 && <p>{t("EFloor")}</p>}
                    { this.state.flag === 5 && <p>{t("EWCount")}</p>}
                    { this.state.flag === 4 && <p>{t("EArea")}</p>}
                    <Input
                        type = 'text'
                        placeholder = {t('Type')}
                        value={this.state.type ? this.state.type : ''}
                        onChange = { (val) => this.setInputValue('type', val)}
                    />
                    <Input
                        type = 'text'
                        placeholder = {t('Floor')}
                        value={this.state.floor ? this.state.floor : ''}
                        onChange = { (val) => this.setInputValue('floor', val)}
                    />
                     <Input
                        type = 'text'
                        placeholder = {t('WCount')}
                        value={this.state.winCount ? this.state.winCount : ''}
                        onChange = { (val) => this.setInputValue('winCount', val)}
                    />
                    <Input
                        type = 'text'
                        placeholder = {t('Area')}
                        value={this.state.area ? this.state.area : ''}
                        onChange = { (val) => this.setInputValue('area', val)}
                    />
                    <Button
                        text = {t('Add')}
                        disabled = {this.state.buttonDisabled}
                        onClick = { () => this.checkCred()}
                    />
                </div>
            </div>
        )
    }
}

export default withTranslation() (AddRForm);
