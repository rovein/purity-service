import React from 'react'
import Input from '../ui/Input'
import Button from '../ui/Button'
import { withTranslation } from 'react-i18next'
import Loader from "react-loader-spinner";

class ConfigureSmartDeviceForm extends React.Component{
  constructor(props) {
    super(props)
    this.state = {
      deviceIp: '',
      serverIp: '',
      serverPort: '',
      placementId: '',
      buttonDisabled: false,
      isLoaded: true
    }
  }

  setInputValue(property, val) {
    this.setState({
      [property]: val
    })
  }

  resetForm(){
    this.setState({
      deviceIp: '',
      serverIp: '',
      serverPort: '',
      placementId: '',
      buttonDisabled: false,
      isLoaded: true
    })
  }

  submitForm(){
    this.setState({
      buttonDisabled: true,
      isLoaded: false
    })

    this.configureDevice();
  }

  async configureDevice() {
    const bytes = this.state.serverIp.split('.');
    try{
      let res = await fetch(
        `http://${this.state.deviceIp}/?byte1=${bytes[0]}&byte2=${bytes[1]}&byte3=${bytes[2]}&byte4=${bytes[3]}&port=${this.state.serverPort}&roomId=${this.state.placementId}`
      )
      if (res.status === 200) {
        setTimeout(() => {
          this.setState({isSuccess: true, isLoaded: true})
        }, 1500);
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
          <h1>{t('ConfigureDevice')}</h1>
          {this.state.isSuccess && <p>{t("SuccessConfiguring")}</p>}
          <Input
            type = 'text'
            placeholder = {t('DeviceIp')}
            value={this.state.deviceIp ? this.state.deviceIp : ''}
            onChange = { (val) => this.setInputValue('deviceIp', val)}
          />
          <Input
            type = 'text'
            placeholder = {t('ServerIp')}
            value={this.state.serverIp ? this.state.serverIp : ''}
            onChange = { (val) => this.setInputValue('serverIp', val)}
          />
          <Input
            type = 'text'
            placeholder = {t('ServerPort')}
            value={this.state.serverPort ? this.state.serverPort : ''}
            onChange = { (val) => this.setInputValue('serverPort', val)}
          />
          <Input
            type = 'text'
            placeholder = {t('PlacementId')}
            value={this.state.placementId ? this.state.placementId : ''}
            onChange = { (val) => this.setInputValue('placementId', val)}
          />
          <Button
            text = {t('Configure')}
            disabled = {this.state.buttonDisabled}
            onClick = { () => this.submitForm()}
          />
        </div>
      </div>
    )
  }
}

export default withTranslation() (ConfigureSmartDeviceForm);
