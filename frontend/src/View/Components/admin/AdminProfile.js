import React from 'react'
import { withTranslation } from 'react-i18next';
import CleaningCard from './AdminClCard';
import AdminCustCart from './AdminCustCart';
import axios from "axios";
import Button from "../ui/Button";

var url = "http://localhost:8080";
const FileDownload = require("js-file-download");

class Profile extends React.Component{
    backup() {
        setTimeout(function() { alert('Файл буде завантажено через декілька секунд'); }, 1);
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
            FileDownload(response.data, "backup_data.sql");
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
                    <div className="profile_back">
                        <p id="cName">{t("Admin")}</p>
                        <Button
                            text={t("Backup")}
                            disabled={false}
                            onClick={(e) => {
                                this.backup();
                            }}
                        />
                    </div>
                    <div className="rooms_back">
                        <p id="EMP" style={adminBack}>{t("CComp")}</p>
                    </div>
                        <div id="rooms_container">
                           <CleaningCard/>
                        </div>
                        <div className="rooms_back">
                            <p id="EMP" style={adminBack}>{t("Comp")}</p>
                        </div>
                        <div id="room_container">
                           <AdminCustCart/>
                        </div>
                </div>
            )
        }
    
}

export default withTranslation()  (Profile);
