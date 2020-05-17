import React from 'react';
import {
  Typography,
  FormControl,
  Select,
  InputLabel,
  MenuItem,
  TextField,
  OutlinedInput,
  Grid,
} from '@material-ui/core';
import { Style, TickType } from '../models/send';
import { DEFAULT_FILTER_OPTIONS, FilterOptions } from '../models/filter';

export interface SendFilterProps {
  onFilterChange: (options: FilterOptions) => void;
}

export default function SendFilter(props: SendFilterProps) {
  const { onFilterChange } = props;
  const [options, setOptions] = React.useState(DEFAULT_FILTER_OPTIONS);

  const styleInputLabel = React.useRef(null);
  const tickTypeInputLabel = React.useRef(null);
  const [styleLabelWidth, setStyleLabelWidth] = React.useState(40);
  const [tickTypeLabelWidth, setTickTypeLabelWidth] = React.useState(60);
  React.useEffect(() => {
    setStyleLabelWidth(styleInputLabel.current.offsetWidth);
    setTickTypeLabelWidth(tickTypeInputLabel.current.offsetWidth);
  }, []);

  type inputChangeEvent = React.ChangeEvent<({ name: string, value: string })>;
  const handleFilterChange = (event: inputChangeEvent) => {
    const newOptions = {
      ...options,
      [event.target.name]: event.target.value,
    };

    setOptions(newOptions);
    onFilterChange(newOptions);
  };

  return (
    <form autoComplete="off">
      <Grid container direction="row" justify="space-between" spacing={1}>

        <Grid item xs>
          <TextField
            label="Name"
            name="name"
            variant="outlined"
            value={options.name}
            InputLabelProps={{ shrink: true }}
            onChange={handleFilterChange}
          />
        </Grid>

        <Grid item xs>
          <FormControl fullWidth variant="outlined">
            <InputLabel shrink ref={styleInputLabel} htmlFor="style-filter">
              <Typography>Style</Typography>
            </InputLabel>
            <Select
              id="style-filter"
              value={options.style}
              onChange={handleFilterChange}
              input={(
                <OutlinedInput
                  notched
                  id="style-filter"
                  name="style"
                  labelWidth={styleLabelWidth}
                />
            )}
            >
              <MenuItem value="all">All</MenuItem>
              <MenuItem value={Style.BOULDER}>{Style.BOULDER}</MenuItem>
              <MenuItem value={Style.SPORT}>{Style.SPORT}</MenuItem>
              <MenuItem value={Style.TRAD}>{Style.TRAD}</MenuItem>
            </Select>
          </FormControl>
        </Grid>

        <Grid item xs>
          <TextField
            label="Grade"
            name="grade"
            variant="outlined"
            value={options.grade}
            InputLabelProps={{ shrink: true }}
            onChange={handleFilterChange}
          />
        </Grid>

        <Grid item xs>
          <FormControl fullWidth variant="outlined">
            <InputLabel shrink ref={tickTypeInputLabel} htmlFor="tickType-filter">
              <Typography>Tick Type</Typography>
            </InputLabel>
            <Select
              id="tickType-filter"
              value={options.tickType}
              onChange={handleFilterChange}
              input={(
                <OutlinedInput
                  notched
                  id="tickType-filter"
                  name="tickType"
                  labelWidth={tickTypeLabelWidth}
                />
            )}
            >
              <MenuItem value="all">All</MenuItem>
              <MenuItem value={TickType.ONSIGHT}>{TickType.ONSIGHT}</MenuItem>
              <MenuItem value={TickType.FLASH}>{TickType.FLASH}</MenuItem>
              <MenuItem value={TickType.REDPOINT}>{TickType.REDPOINT}</MenuItem>
            </Select>
          </FormControl>
        </Grid>

        <Grid item xs>
          <TextField
            label="Location"
            name="location"
            variant="outlined"
            value={options.location}
            InputLabelProps={{ shrink: true }}
            onChange={handleFilterChange}
          />
        </Grid>

      </Grid>
    </form>
  );
}
