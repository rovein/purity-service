import {DELAY} from "./Constants";

export default function doWithDelay(func) {
    setTimeout(() => {
        func()
    }, DELAY)
}
