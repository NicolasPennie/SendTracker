export type HeaderId = 'name' | 'style' | 'grade' | 'tickType' | 'location';

export interface Header {
  id: HeaderId;
  label: string;
}
