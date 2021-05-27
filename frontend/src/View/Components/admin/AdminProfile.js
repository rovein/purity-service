import React from 'react'
import { withTranslation } from 'react-i18next';
import CleaningCard from './AdminCleaningProvidersTable';
import AdminCustCart from './AdminPlacementOwnersTable';
import axios from "axios";
import Button from "../ui/Button";
import SweetAlert from "react-bootstrap-sweetalert";

var url = "http://localhost:8080";
const FileDownload = require("js-file-download");

class Profile extends React.Component{

    constructor(props) {
        super(props);
        this.state = {
            backupButtonClicked: false
        };
        this.backup = this.backup.bind(this)
        this.closeBackupAlert = this.closeBackupAlert.bind(this)
    }

    componentDidMount() {
        const backupNav = document.getElementById("BC");
        backupNav.onclick = this.backup
    }

    backup() {
       this.setState({backupButtonClicked: true})
        axios({
            url: `${url}/admin/backup`,
            method: "GET",
            headers: {
                Accept: "application/octet-stream",
                "Content-Type": "application/octet-stream",
                "Content-Disposition": "attachment; filename='backup_data.sql'",
                Authorization: "Bearer " + localStorage.getItem("Token"),
            },
            responseType: "blob", // Important
        }).then((response) => {
            FileDownload(response.data, `backup_data.sql`);
        });
    }

    render() {
        localStorage.removeItem("Email")
        localStorage.removeItem("Role")
        const adminBack = {
            textAlign: "center",
            fontSize: "25px",
            padding: "0 0 0 0"
        }
            const {t} = this.props
            return(
                <div>
                    <div className="rooms_back" style={{border: "1px solid black"}}>
                        <p id="EMP" style={adminBack}>{t("CComp")}</p>
                    </div>
                        <div id="rooms_container">
                           <CleaningCard/>
                        </div>
                        <div className="rooms_back" style={{border: "1px solid black"}}>
                            <p id="EMP" style={adminBack}>{t("Comp")}</p>
                        </div>
                        <div id="room_container">
                           <AdminCustCart/>
                        </div>
                    {this.state.backupButtonClicked &&
                    <SweetAlert success title={t("Success")}
                                onConfirm={this.closeBackupAlert}
                                onCancel={this.closeBackupAlert}
                                customButtons={
                                    <React.Fragment>
                                        <button
                                            className="w3-btn w3-indigo w3-round-small w3-medium"
                                            onClick={this.closeBackupAlert}
                                        >OK</button>
                                    </React.Fragment>
                                }
                    >
                        {t("BackupWillDownload")}
                    </SweetAlert>}
                </div>
            )
        }

        closeBackupAlert() {
            this.setState({backupButtonClicked: false})
        }
    
}

export default withTranslation()  (Profile);
