import React from 'react';
import Typography from '@material-ui/core/Typography';
import Paper from '@material-ui/core/Paper';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import { makeStyles } from '@material-ui/core/styles';

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
            marginTop: theme.spacing(2),
            marginBottom: theme.spacing(2)
        }
    });
});

export default () => {
    const classes = useStyles();

    return (
        <React.Fragment>
            <Typography className={classes.header} variant="h4" align="left">
                My Sends
            </Typography>
            <Paper className={classes.sendBox}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Name</TableCell>
                            <TableCell align="right">Type</TableCell>
                            <TableCell align="right">Grade</TableCell>
                            <TableCell align="right">Style</TableCell>
                            <TableCell align="right">Location</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {sends.map(s => (
                            <TableRow key={`${s.name}-${s.location}`}>
                                <TableCell component="th" scope="row">{s.name}</TableCell>
                                <TableCell align="right">{s.type}</TableCell>
                                <TableCell align="right">{s.grade}</TableCell>
                                <TableCell align="right">{s.style}</TableCell>
                                <TableCell align="right">{s.location}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </Paper>
        </React.Fragment>
    );
};