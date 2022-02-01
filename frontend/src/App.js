import React from 'react'
import './App.css';
import { observer } from 'mobx-react'
import {
  Route,
  Switch,
  BrowserRouter,
  Redirect} from "react-router-dom"
import SignIn from './View/Components/navigation/Login'
import SignUp from './View/Components/navigation/SignUp'
import Profile from './View/Components/navigation/Profile'
import Search from './View/Components/navigation/Search'
import { withTranslation } from 'react-i18next'
import Edit from './View/Components/navigation/Edit';
import AddRoom from './View/Components/navigation/placement/AddPlacement'
import AddService from './View/Components/navigation/service/AddService'
import Contract from './View/Components/navigation/contract/Contract'
import EditService from './View/Components/navigation/service/EditService';
import SignContract from './View/Components/contract/CreateContract';
import EditRoom from './View/Components/navigation/placement/EditPlacement';
import ConfigureDevice from "./View/Components/navigation/ConfigureDevice";
import Info from "./View/Components/navigation/PlacementInfo";
import DataTableUsageExample from "./View/Components/ui/DataTable";

class App extends React.Component{
  constructor(props) {
    super(props)
    this.state = {
        value: "en"
    }
 }
  render(){
      return(
        <div className="App">
          <BrowserRouter>
            <Switch>
              <Route path='/login' component={SignIn} />
              <Route path='/signup' component={SignUp} />

              <Route path='/profile' component={Profile} />
              <Route path='/edit' component={Edit} />

              <Route path='/search' component={Search} />
              <Route path='/more' component={Info} />

              <Route path='/add_room' component={AddRoom} />
              <Route path='/edit_room' component={EditRoom} />

              <Route path='/add_service' component={AddService} />
              <Route path='/edit_service' component={EditService} />

              <Route path='/contracts' component={Contract} />
              <Route path='/sign_contract' component={SignContract}/>

              <Route path='/configure-smart-device' component={ConfigureDevice} />
            <Redirect from='/' to='/login'/>
          </Switch>
        </BrowserRouter>
      </div>
      )
  }
}

export default withTranslation() (observer(App));
