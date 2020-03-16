import React from 'react';
import Head from 'next/head';
import AppBar from '@material-ui/core/AppBar';
import Box from '@material-ui/core/Box';
import Button from '@material-ui/core/Button';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';

const padding = 4;
const footerHeight = 8;
const useStyles = makeStyles((theme) => ({
  shell: {
    position: 'relative',
    minHeight: '100%',
  },
  navbar: {
    padding: theme.spacing(1),
    paddingLeft: theme.spacing(padding),
    paddingRight: theme.spacing(padding),
  },
  user: {
    float: 'right',
  },
  content: {
    padding: theme.spacing(padding),
    paddingBottom: theme.spacing(footerHeight),
    overflow: 'hidden',
  },
  footer: {
    position: 'absolute',
    bottom: 0,
    width: '100%',
    height: theme.spacing(footerHeight),
    padding: theme.spacing(padding),
  },
}));

export default function (props: { children: JSX.Element }) {
  const { children } = props;
  const classes = useStyles(props);

  return (
    <>
      <Head>
        <title>SendTracker</title>
        <meta charSet="utf-8" />
        <meta name="viewport" content="initial-scale=1.0, width=device-width" />
      </Head>

      <Box className={classes.shell}>
        <AppBar className={classes.navbar} position="relative">
          <Grid container spacing={2} justify="space-between">
            <Grid item sm={1}>
              <Typography component="span" variant="h5" align="left">SendTracker</Typography>
            </Grid>
            <Grid item sm={1}>
              <Button className={classes.user} variant="text" color="inherit">Login</Button>
            </Grid>
          </Grid>
        </AppBar>

        <Box className={classes.content}>
          {children}
        </Box>

        <Box className={classes.footer}>
          <Typography variant="caption" align="center">
              Nicolas Pennie &copy; 2019
          </Typography>
        </Box>
      </Box>
    </>
  );
}
