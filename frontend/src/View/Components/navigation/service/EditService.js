import React from 'react'
import Header from '../../auth/HeaderAuth'
import EditService from '../../cleaning-provider/EditServiceForm'
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
