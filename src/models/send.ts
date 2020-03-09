export enum Style {
  BOULDER = 'Boulder',
  SPORT = 'Sport',
  TRAD = 'Trad'
}

export enum TickType {
  ONSIGHT = 'Onsight',
  FLASH = 'Flash',
  REDPOINT = 'Redpoint'
}

export interface Grade {}

export interface Send {
  name: string;
  style: Style;
  grade: Grade;
  tickType: TickType;
  location: string;
}
