import React from 'react';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import { styled, makeStyles } from '@material-ui/core/styles';

/* TODO: Replace with TS Enum after migration */
const SPORT = 'Sport';
const TRAD = 'Trad';
const BOULDER = 'Boulder';

/* TODO: Replace with TS Enum after migration */
const ONSIGHT = 'Onsight';
const FLASH = 'Flash';
const REDPOINT = 'Redpoint';

/* TODO: Move to mock */
const sends = [
    { 
        name: 'Pure Imagination',
        type: SPORT,
        grade: '5.14c',
        style: REDPOINT,
        location: 'Red River Gorge'
    },
    {
        name: 'Your Wife',
        type: SPORT,
        grade: '5.11b',
        style: FLASH,
        location: 'Down Under'
    },
    {
        name: 'The Process',
        type: BOULDER,
        grade: 'V16',
        style: ONSIGHT,
        location: 'The Buttermilks'
    },
    {
        name: 'Scarface',
        type: TRAD,
        grade: '5.11',
        style: REDPOINT,
        location: 'Indian Creek'
    }
];

const useStyles = makeStyles(theme => {
    return ({
        header: {
            paddingBottom: theme.spacing(1)
        },
        sendBox: {
            paddingTop: theme.spacing(2),
            paddingBottom: theme.spacing(2)
        },
        sendItem: {
            marginLeft: theme.spacing(4),
            marginRight: theme.spacing(4),
            padding: theme.spacing(1)
        }
    });
});

const Send = ({ send }) => {
    const classes = useStyles();

    return (
        <Grid item>
            <Paper className={classes.sendItem}>
                <Typography variant="body1" align="left">
                    {send.name}
                </Typography>
                <Typography variant="body2" align="left">
                    {send.grade}
                </Typography>
            </Paper>
        </Grid>
    );
};

export default () => {
    const classes = useStyles();

    return (
        <React.Fragment>
            <Typography className={classes.header} variant="h4" align="left">
                My Sends
            </Typography>
            <Paper className={classes.sendBox}>
                <Grid container spacing={2} direction="column">

                    { sends.map(s => (<Send send={s} />)) }
                    
                    <Grid item className={classes.sendItem}>
                        <Button variant="contained" color="primary">Add</Button>
                    </Grid>
                </Grid>
            </Paper>
        </React.Fragment>
    );
};