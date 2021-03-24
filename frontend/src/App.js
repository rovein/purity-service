import React from 'react'
import './App.css';
import { observer } from 'mobx-react'
import {
  Route,
  Switch,
  BrowserRouter,
  Redirect} from "react-router-dom"
import SignIn from './View/Login'
import SignUp from './View/SignUp'
import Profile from './View/Profile'
import Search from './View/Search'
import { withTranslation } from 'react-i18next'
import Edit from './View/Edit';
import MoreInfo from './View/MoreInfo'
import AddRoom from './View/AddRoom'
import AddService from './View/AddService'
import Contract from './View/Contract'
import EditService from './View/EditService';
import SignContract from './View/SignContract';
import EditRoom from './View/EditRoom';

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
              <Route path='/more' component={MoreInfo} />
              <Route path='/add_room' component={AddRoom} />
              <Route path='/edit_room' component={EditRoom} />
              <Route path='/add_service' component={AddService} />
              <Route path='/edit_service' component={EditService} />
              <Route path='/contracts' component={Contract} />
              <Route path='/sign_contract' component={SignContract}/>
            <Redirect from='/' to='/login'/>
          </Switch>
        </BrowserRouter>
      </div>
      )
  }
}

export default withTranslation() (observer(App));