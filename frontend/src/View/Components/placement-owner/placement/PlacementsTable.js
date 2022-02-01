import React, {useEffect, useState} from "react";
import {useTranslation, withTranslation} from "react-i18next";
import jwt_decode from "jwt-decode";
import Moment from "moment";
import localization from "moment/locale/uk";
import axios from "../../util/Api";
import DefaultLoader from "../../ui/Loader";
import DataTableComponent from "../../ui/DataTable";
import * as Constants from "../../util/Constants";

function PlacementsTable() {
    const baseUrl = Constants.SERVER_URL;
    let decoded
    if (localStorage.getItem("Token") != null) {
        decoded = jwt_decode(localStorage.getItem("Token"));
    }

    const {t} = useTranslation();
    const dataUrl = `${baseUrl}/placement-owners/${decoded.email}/placements`
    const [data, setData] = useState([])
    const [isLoaded, setIsLoaded] = useState(false)

    useEffect(() => {
        axios.get(dataUrl)
            .then(result => {
                const data = result.data
                data.forEach(entry => entry.lastCleaning = localTime(entry.lastCleaning))
                setData(data)
                setIsLoaded(true)
            })
    }, [])

    const columns = React.useMemo(
        () => [
            {
                Header: 'ID',
                accessor: 'id',
            },
            {
                Header: "Type",
                accessor: 'placementType',
            },
            {
                Header: "Floor",
                accessor: 'floor'
            },
            {
                Header: "WCount",
                accessor: 'windowsCount'
            },
            {
                Header: "Area",
                accessor: 'area'
            },
            {
                Header: "Date",
                accessor: 'lastCleaning'
            }
        ],
        []
    )

    function editEntity(id) {
        localStorage.setItem("roomId", id);
        window.location.href = "./edit_room";
    }

    function openMore(id) {
        localStorage.setItem("Id", id);
        window.location.href = "./more";
    }

    const operations = [
        {
            "name": "More",
            "onClick": openMore,
            "className": "w3-btn w3-indigo w3-round-small w3-medium",
            "onClickPassParameter": "id"
        },
        {
            "name": "Edit",
            "onClick": editEntity,
            "className": "w3-btn w3-khaki w3-round-small w3-medium",
            "onClickPassParameter": "id"
        },
        {
            "name": "Delete",
            "className": "w3-btn w3-red w3-round-small w3-medium",
            "onClickPassParameter": "id",
            "url": "placement-owners/placements/{id}",
        }
    ]

    if (!isLoaded) return <DefaultLoader/>;
    return <DataTableComponent displayData={data} displayColumns={columns} operations={operations}/>
}

function localTime(date) {
    if (localStorage.getItem("i18nextLng") === "EN") {
        return Moment(date).locale("en").format("LLL");
    } else if (localStorage.getItem("i18nextLng") === "UA") {
        return Moment(date).locale("uk", localization).format("LLL");
    }
}

export default withTranslation()(PlacementsTable);
