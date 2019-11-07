import React from 'react';
import Head from 'next/head';
import AppBar from '@material-ui/core/AppBar';
import Box from '@material-ui/core/Box';
import Button from '@material-ui/core/Button';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import { makeStyles, styled } from '@material-ui/core/styles';

const AppContainer = styled(Box)({
  width: '100vw',
  height: '100vh'
})

const AppFooter = styled(Box)({
  position: 'absolute',
  bottom: 0
})

const useStyles = makeStyles(theme => ({
  appbar: {
    padding: theme.spacing(1),
    paddingLeft: theme.spacing(4),
    paddingRight: theme.spacing(4)
  },
  appbarUser: {
    float: 'right'
  },
  appFooter: {
    padding: theme.spacing(1),
    paddingLeft: theme.spacing(4),
    paddingRight: theme.spacing(4)
  }
}));


export default function() {

  const classes = useStyles();

  return (
    <AppContainer>
      <Head>
        <title>SendTracker</title>
        <meta charSet='utf-8' />
        <meta name='viewport' content='initial-scale=1.0, width=device-width' />
      </Head>

      <AppBar className={classes.appbar} position="relative">
        <Grid container spacing={2} justify="space-between">
          <Grid item sm={1}>
            <Typography component="span" variant="h5" align="left">SendTracker</Typography>
          </Grid>
          <Grid item sm={1}>
            <Button className={classes.appbarUser} variant="text" color="inherit">Login</Button>
          </Grid>
        </Grid>
      </AppBar>

      <AppFooter className={classes.appFooter}>
        <Typography variant="caption" align="center">
          Nicolas Pennie &copy; 2019
        </Typography>
      </AppFooter>
    </AppContainer>
  );  
}
