export function checkType(type) {
    let rType = new RegExp('^([А-Яа-яё]+)|([a-z]+)$');
    if(!rType.test(type)){
        this.setState({flag: 2});
        return false
    }
    return true
}

export function checkFloor(floor) {
    let rFloor = new RegExp('^([0-9]+)$');
    if(!rFloor.test(floor)){
        this.setState({flag: 3});
        return false
    }
    return true
}

export function checkArea(area) {
    let rArea = new RegExp('^([0-9]+)$');
    if(!rArea.test(area)){
        this.setState({flag: 4});
        return false
    }
    return true
}

export function checkWCount(winCount) {
    let rWCount = new RegExp('^([0-9]+)$');
    if(!rWCount.test(winCount)){
        this.setState({flag: 5});
        return false
    }
    return true
}
