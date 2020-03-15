import React from 'react';
import {
  Box,
  Paper,
  Typography,
  makeStyles,
} from '@material-ui/core';
import SendTable from './SendTable';
import SendToolbar from './SendToolbar';
import MOCK_SENDS from '../mock/sends';

const useStyles = makeStyles((theme) => ({
  header: {
    paddingBottom: theme.spacing(1),
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

export default (props: {}) => {
  const classes = useStyles(props);
  const [sends] = React.useState(MOCK_SENDS);

  return (
    <Box className={classes.sendBox}>
      <Typography className={classes.header} variant="h4" align="left">
        My Sends
      </Typography>
      <Paper className={classes.table}>
        <SendToolbar />
        <SendTable sends={sends} />
      </Paper>
    </Box>
  );
};
