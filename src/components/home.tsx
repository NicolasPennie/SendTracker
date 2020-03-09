import React from 'react';
import {
  Box,
  IconButton,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  TableSortLabel,
  TablePagination,
  Toolbar,
  Typography,
  makeStyles,
  Tooltip,
  FormControl,
  Select,
  InputLabel,
  MenuItem,
  TextField,
  OutlinedInput,
  Grow,
} from '@material-ui/core';
import { FilterListRounded as FilterIcon } from '@material-ui/icons';

import SendTable from './SendTable/SendTable';
import { stableSort, getSorting, SortOrder } from './SendTable/sort';
import { Style } from '../models/send';
import { MOCK_SENDS } from '../mock/sends';

const useStyles = makeStyles((theme) => ({
  header: {
    paddingBottom: theme.spacing(1),
  },
  filterToolbar: {
    height: 80,
  },
  filterForm: {
    '& > *': {
      margin: theme.spacing(1),
      width: 120,
    },
  },
  filterIcon: {
    position: 'absolute',
    right: theme.spacing(1),
  },
  sendBox: {
    margin: 'auto',
    [theme.breakpoints.down('md')]: {
      width: '100%',
    },
    [theme.breakpoints.up('md')]: {
      width: '800px',
    },
  },
  table: {
    marginTop: theme.spacing(2),
  },
}));

interface SendFilterProps {}

export default (props: {}) => {
  const classes = useStyles(props);
  const [sends] = React.useState(MOCK_SENDS);
  const [filterVisible, setFilterVisible] = React.useState(false);

  return (
    <Box className={classes.sendBox}>
      <Typography className={classes.header} variant="h4" align="left">
        My Sends
      </Typography>
      <Paper className={classes.table}>
        <Toolbar className={classes.filterToolbar}>
          <div />
          <Grow in={filterVisible}>
            <div>
              <SendFilter />
            </div>
          </Grow>
          <Tooltip
            className={classes.filterIcon}
            title={filterVisible ? 'Hide Filter' : 'Show Filter'}
          >
            <IconButton
              aria-label="filter"
              onClick={() => setFilterVisible(!filterVisible)}
            >
              <FilterIcon />
            </IconButton>
          </Tooltip>
        </Toolbar>
        <SendTable sends={sends} />
      </Paper>
    </Box>
  );
};
