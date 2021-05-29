import React from 'react'
import Header from '../../auth/HeaderAuth'
import EditRoomForm from '../../placement-owner/placement/EditPlacementForm'
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
