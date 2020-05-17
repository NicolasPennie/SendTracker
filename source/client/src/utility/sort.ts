export type SortOrder = 'asc' | 'desc';
export type CompareFn<T> = (x: T, y: T) => number;
export type SortFn<T> = (a: [T, number], b: [T, number]) => number;

function desc<T>(a: T, b: T, orderBy: string | number): number {
  if (b[orderBy] < a[orderBy]) {
    return -1;
  }
  if (b[orderBy] > a[orderBy]) {
    return 1;
  }
  return 0;
}

export function getSorting<T>(
  order: SortOrder,
  orderBy: string | number,
): CompareFn<T> {
  return order === 'desc'
    ? (a, b) => desc(a, b, orderBy)
    : (a, b) => -desc(a, b, orderBy);
}

export function stableSort<T>(array: T[], cmp: CompareFn<T>): T[] {
  const stableSorter: SortFn<T> = ([a, indexA], [b, indexB]) => {
    const order = cmp(a, b);
    return order !== 0 ? order : indexA - indexB;
  };

  return array
    .map((el, index) => [el, index])
    .sort(stableSorter)
    .map((el) => el[0] as T);
}

