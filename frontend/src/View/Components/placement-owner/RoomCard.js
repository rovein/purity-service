import React from "react";
import Button from "../Button";
import { withTranslation } from "react-i18next";
import jwt_decode from "jwt-decode";
import Moment from "moment";
import localization from "moment/locale/uk";

if (localStorage.getItem("Token") != null) {
  var token = localStorage.getItem("Token");
  var decoded = jwt_decode(token);
}

var url = "http://localhost:8080";

class Card extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      error: null,
      isLoaded: false,
      rooms: [],
      date: "",
    };
  }

  render() {
    const { t } = this.props;
    const { error, isLoaded, rooms } = this.state;
    if (error) {
      return (
        <div className="additional">
          {t("Failiture")}: {error.message}
        </div>
      );
    } else if (!isLoaded) {
      return <div className="additional">{t("Loading")}...</div>;
    } else {
      return (
          <table className="w3-table-all w3-centered">
            <thead>
            <tr>
              <th>ID</th>
              <th>{t("Type")}</th>
              <th> {t("Floor")}</th>
              <th> {t("WCount")}</th>
              <th>{t("Area")}</th>
              <th>{t("Date")}</th>
              <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            {rooms.sort((oneRoom, anotherRoom) => {
              return oneRoom.id - anotherRoom.id
            }).map(this.renderCard)}
            </tbody>
          </table>
      );
    }
  }

  renderCard = (room) => {
    const { t } = this.props;
    const columnStyle = {verticalAlign: "middle"};
    this.localTime(room.lastCleaning);
    return (
        <tr>
          <td style={columnStyle}>{room.id}</td>
          <td style={columnStyle}>{room.placementType}</td>
          <td style={columnStyle}>{room.floor}</td>
          <td style={columnStyle}>{room.windowsCount}</td>
          <td style={columnStyle}>{room.area}</td>
          <td style={columnStyle}>{this.state.date}</td>
          <td>
            <Button
                className='w3-btn w3-indigo w3-round-small w3-medium'
                text={t("More")}
                onClick={() => {
                  localStorage.setItem("Id", room.id);
                  window.location.href = "./more";
                }}
            /> &nbsp;
            <Button
                className='w3-btn w3-khaki w3-round-small w3-medium'
                text={t("Edit")}
                onClick={(e) => {
                  localStorage.setItem("roomId", room.id);
                  window.location.href = "./edit_room";
                }}
            /> &nbsp;
            <Button
                className='w3-btn w3-red w3-round-small w3-medium'
                text={t("Delete")} onClick={() => this.deleteRoom(room.id)} />
          </td>
        </tr>
    );
  };

  componentDidMount() {
    fetch(`${url}/placement-owners/${decoded.email}/placements`, {
      method: "get",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: "Bearer " + localStorage.getItem("Token"),
      },
    })
      .then((res) => res.json())
      .then(
        (result) => {
          this.setState({
            isLoaded: true,
            rooms: result,
          });
        },
        (error) => {
          this.setState({
            isLoaded: true,
            error,
          });
        }
      );
  }

  localTime(date) {
    if (localStorage.getItem("i18nextLng") === "EN") {
      this.state.date = Moment(date).locale("en").format("LLL");
    } else if (localStorage.getItem("i18nextLng") === "UA") {
      this.state.date = Moment(date).locale("uk", localization).format("LLL");
    }
  }

  deleteRoom(id) {
    fetch(`${url}/placement-owners/placements/${id}`, {
      method: "delete",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: "Bearer " + token,
      },
    }).then(
      (result) => {
        window.location.reload();
      },
      (error) => {
        this.setState({
          isLoaded: true,
          error,
        });
      }
    );
  }
}

export default withTranslation()(Card);
