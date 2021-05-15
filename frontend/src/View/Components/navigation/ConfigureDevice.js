import React from 'react'
import Header from '../auth/HeaderAuth'
import ConfigureSmartDevice from "../admin/ConfigureSmartDevice";
import AddRForm from "../placement-owner/AddRForm";

class ConfigureDevice extends React.Component {

  render() {
        return (
          <div className="signIn">
            <Header/>
            <div className="container">
              <ConfigureSmartDevice/>
            </div>
          </div>
        )

  }
}

export default ConfigureDevice;
