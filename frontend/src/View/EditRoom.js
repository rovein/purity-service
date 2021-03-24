import React from 'react'
import Header from './Components/auth/HeaderAuth'
import EditRoomForm from './Components/placement-owner/EditRoomForm'
class Edit extends React.Component{
    
    render() { 
        return(
            <div className="signIn">
            <Header/>
                <div className="container">
                    <EditRoomForm/>
            </div>
        </div>
        )
    }
}

export default Edit;
