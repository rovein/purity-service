import React from 'react'
import Header from './Components/auth/HeaderAuth'
import jwt_decode from "jwt-decode"
import CompanyProfile from './Components/placement-owner/CompanyProfile'
import CleaningProfile from './Components/cleaning-provider/CleaningProfile'
import AdminProfile from './Components/admin/AdminProfile'

if (localStorage.getItem("Token") != null) {
    var token = localStorage.getItem("Token")
    var decoded = jwt_decode(token)
}

class Profile extends React.Component {

    render() {

        if (localStorage.getItem("Token") == null) {
            window.location.href = './'
        } else {
            if (decoded.role === "PLACEMENT_OWNER") {
                return (
                    <div className="profile">
                        <Header/>
                        <CompanyProfile/>
                    </div>
                )
            } else if (decoded.role === "CLEANING_PROVIDER") {
                return (
                    <div className="profile">
                        <Header/>
                        <CleaningProfile/>
                    </div>
                )
            } else if (decoded.role === "ADMIN") {
                return (
                    <div className="profile">
                        <Header/>
                        <AdminProfile/>
                    </div>
                )
            }
        }
    }
}

export default Profile;
