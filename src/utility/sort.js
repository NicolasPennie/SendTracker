export function stableSort(array, cmp) {
    const stableSorter = ([a, indexA], [b, indexB]) => {
        const order = cmp(a, b);
        return order !== 0 ? order : indexA - indexB;
    }

    return array
        .map((el, index) => [el, index])
        .sort(stableSorter)
        .map(el => el[0]);
}

export function getSorting(order, orderBy) {
    return order === 'desc' ? 
        (a, b) => desc(a, b, orderBy) : 
        (a, b) => -desc(a, b, orderBy);
}

function desc(a, b, orderBy) {
    if (b[orderBy] < a[orderBy]) {
      return -1;
    }
    if (b[orderBy] > a[orderBy]) {
      return 1;
    }
    return 0;
}