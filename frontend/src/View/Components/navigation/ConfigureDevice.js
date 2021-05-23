import React from 'react'
import Header from '../auth/HeaderAuth'
import ConfigureSmartDevice from "../admin/ConfigureSmartDevice";

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
