import { Send } from './send';

export interface FilterOptions {
  name?: string;
  style?: string;
  grade?: string;
  tickType?: string;
  location?: string;
}

export const DEFAULT_FILTER_OPTIONS: FilterOptions = {
  name: '',
  style: 'all',
  grade: '',
  tickType: 'all',
  location: '',
};

export class Filter {
  constructor(public options: FilterOptions = DEFAULT_FILTER_OPTIONS) { }

  /**
   * Applies the filter to the given array of sends.
   * Every existing filter option must be included in the Send.
   * @param sends Array of sends to be filtered.
   * @returns Filtered sends.
   */
  public apply(sends: Send[]) {
    const { options } = this;

    // TODO: Create re-usable filter handlers per property
    return sends.filter(send => {
      const validators = [];
      validators.push(!options.name || send.name.includes(options.name));
      validators.push(options.style === 'all' || send.style.includes(options.style));
      validators.push(!options.grade || send.grade.includes(options.grade));
      validators.push(options.tickType === 'all' || send.tickType.includes(options.tickType));
      validators.push(!options.location || send.location.includes(options.location));
      return validators.reduce((valid, condition) => valid && condition);
    });
  }
}
