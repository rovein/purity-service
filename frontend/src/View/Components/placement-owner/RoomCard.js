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
      return <div className="grid">{rooms.map(this.renderCard)}</div>;
    }
  }

  renderCard = (room) => {
    const { t } = this.props;
    this.localTime(room.lastCleaning);
    return (
      <div className="card text-center">
        <div className="crd-body text-dark" id={room.id}>
          <h4 className="card-title">
            {t("Type")}: {room.placementType}
          </h4>
          <p className="card-text text-secondary">
            {t("Floor")}: {room.floor}
          </p>
          <p className="card-text text-secondary">
            {t("WCount")}: {room.windowsCount}
          </p>
          <p className="card-text text-secondary">
            {t("Area")}: {room.area} {t("Measure")}
          </p>
          <p className="card-text text-secondary">
            {t("Date")}: {this.state.date}
          </p>
          <Button
            text={t("More")}
            onClick={() => {
              localStorage.setItem("Id", room.id);
              window.location.href = "./more";
            }}
          />
          <Button
            text={t("Edit")}
            onClick={(e) => {
              localStorage.setItem("roomId", room.id);
              window.location.href = "./edit_room";
            }}
          />
          <Button text={t("Delete")} onClick={() => this.deleteRoom(room.id)} />
        </div>
      </div>
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
