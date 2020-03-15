import React from 'react';
import {
  IconButton,
  Toolbar,
  makeStyles,
  Tooltip,
  Grid,
} from '@material-ui/core';
import { FilterListRounded as FilterIcon } from '@material-ui/icons';
import SendFilter from './SendFilter';

const useStyles = makeStyles((theme) => ({
  toolbar: {
    paddingTop: theme.spacing(2),
    paddingLeft: theme.spacing(2),
    paddingRight: theme.spacing(2),
  },
  filterIcon: {
    right: theme.spacing(1),
  },
}));

export default function SendToolbar(props: {}) {
  const classes = useStyles(props);
  const [filterVisible, setFilterVisible] = React.useState(false);

  return (
    <Toolbar disableGutters variant="dense" className={classes.toolbar}>
      <Grid container direction="column">

        <Grid container justify="flex-end" alignItems="stretch">
          <Grid item>
            <Tooltip
              className={classes.filterIcon}
              title={filterVisible ? 'Hide Filter' : 'Show Filter'}
            >
              <IconButton
                aria-label="filter"
                edge="end"
                onClick={() => setFilterVisible(!filterVisible)}
              >
                <FilterIcon />
              </IconButton>
            </Tooltip>
          </Grid>
        </Grid>

        {filterVisible && (
          <Grid item>
            <SendFilter />
          </Grid>
        )}

      </Grid>
    </Toolbar>
  );
}
