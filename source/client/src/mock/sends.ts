import { Style, TickType, Send } from '../models/send';

const { BOULDER, SPORT, TRAD } = Style;
const { ONSIGHT, FLASH, REDPOINT } = TickType;

const MOCK_SENDS: Send[] = [
  {
    name: 'Pure Imagination',
    style: SPORT,
    grade: '5.14c',
    tickType: REDPOINT,
    location: 'Red River Gorge',
  },
  {
    name: 'Your Wife',
    style: SPORT,
    grade: '5.11b',
    tickType: FLASH,
    location: 'Down Under',
  },
  {
    name: 'The Process',
    style: BOULDER,
    grade: 'V16',
    tickType: ONSIGHT,
    location: 'The Buttermilks',
  },
  {
    name: 'Dreamcatcher',
    style: SPORT,
    grade: '5.14d',
    tickType: REDPOINT,
    location: 'Squamish',
  },
  {
    name: 'The Finnish Line',
    style: BOULDER,
    grade: 'V15',
    tickType: REDPOINT,
    location: 'Rocklands',
  },
  {
    name: 'China Doll',
    style: TRAD,
    grade: '5.14-',
    tickType: REDPOINT,
    location: 'RMNP',
  },
  {
    name: 'Freerider',
    style: TRAD,
    grade: '5.12b',
    tickType: FLASH,
    location: 'Yosemite',
  },
  {
    name: 'Silence',
    style: SPORT,
    grade: '5.15d',
    tickType: REDPOINT,
    location: 'Flatanger',
  },
  {
    name: 'Rainbow Rocket',
    style: BOULDER,
    grade: 'V11',
    tickType: ONSIGHT,
    location: 'Fontainebleau',
  },
  {
    name: 'Just Do It',
    style: SPORT,
    grade: '5.14c',
    tickType: ONSIGHT,
    location: 'Smith Rock',
  },
  {
    name: 'Breakfast Burrito',
    style: SPORT,
    grade: '5.10c',
    tickType: REDPOINT,
    location: 'Red River Gorge',
  },
];

export default MOCK_SENDS;
