export function checkType(type) {
    let rType = new RegExp('^([А-Яа-яё]+)|([a-z]+)$');
    if(!rType.test(type)){
        return 2
    }
    return 0
}

export function checkFloor(floor) {
    let rFloor = new RegExp('^([0-9]+)$');
    if(!rFloor.test(floor)){
        return 3
    }
    return 0
}

export function checkArea(area) {
    let rArea = new RegExp('^([0-9]+)$');
    if(!rArea.test(area)){
        return 4
    }
    return 0
}

export function checkWCount(winCount) {
    let rWCount = new RegExp('^([0-9]+)$');
    if(!rWCount.test(winCount)){
        return 5
    }
    return 0
}
