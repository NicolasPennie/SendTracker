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
import { Style } from '../models/send';

export default function SendFilter() {
  const [filter, setFilter] = React.useState({
    name: '',
    style: 'all',
    grade: '',
    tickType: 'all',
    location: '',
  });

  const inputLabel = React.useRef(null);
  const [labelWidth, setLabelWidth] = React.useState(40);
  React.useEffect(() => {
    setLabelWidth(inputLabel.current.offsetWidth);
  }, []);

  type inputChangeEvent = React.ChangeEvent<({ name: string, value: string })>;
  const handleFilterChange = (event: inputChangeEvent) => {
    setFilter({
      ...filter,
      [event.target.name]: event.target.value,
    });
  };

  return (
    <form autoComplete="off">
      <Grid container direction="row" justify="space-between" spacing={1}>

        <Grid item xs>
          <TextField
            label="Name"
            name="name"
            variant="outlined"
            value={filter.name}
            InputLabelProps={{ shrink: true }}
            onChange={handleFilterChange}
          />
        </Grid>

        <Grid item xs>
          <FormControl fullWidth variant="outlined">
            <InputLabel shrink ref={inputLabel} htmlFor="style-filter">
              <Typography>Style</Typography>
            </InputLabel>
            <Select
              id="style-filter"
              value={filter.style}
              onChange={handleFilterChange}
              input={(
                <OutlinedInput
                  notched
                  id="style-filter"
                  name="style"
                  labelWidth={labelWidth}
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
            value={filter.grade}
            InputLabelProps={{ shrink: true }}
            onChange={handleFilterChange}
          />
        </Grid>

        <Grid item xs>
          <FormControl fullWidth variant="outlined">
            <InputLabel shrink ref={inputLabel} htmlFor="tickType-filter">
              <Typography>Style</Typography>
            </InputLabel>
            <Select
              id="tickType-filter"
              value={filter.tickType}
              onChange={handleFilterChange}
              input={(
                <OutlinedInput
                  notched
                  id="tickType-filter"
                  name="tickType"
                  labelWidth={labelWidth}
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
            label="Location"
            name="location"
            variant="outlined"
            value={filter.location}
            InputLabelProps={{ shrink: true }}
            onChange={handleFilterChange}
          />
        </Grid>

      </Grid>
    </form>
  );
}
