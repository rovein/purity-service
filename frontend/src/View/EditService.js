import React from 'react'
import Header from './Components/auth/HeaderAuth'
import EditService from './Components/cleaning-provider/EditServiceForm'
class Edit extends React.Component{
    
    render() { 
        return(
            <div className="signIn">
            <Header/>
                <div className="container">
                    <EditService/>
            </div>
        </div>
        )
    }
}

export default Edit;
