import React from 'react'
import Loader from "react-loader-spinner";

function DefaultLoader() {
    return (
        <div className="centered">
            <Loader
                type="Oval" //Audio Oval ThreeDots
                color="#4B0082"
                height={325}
                width={325}
                timeout={10000}
            />
        </div>
    )
}

export default DefaultLoader;
