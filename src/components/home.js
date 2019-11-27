import React from 'react';
import PropTypes from 'prop-types';
import { 
    Typography, 
    Paper, 
    Table, 
    TableBody,
    TableCell,
    TableHead,
    TableRow,
    TableSortLabel,
    makeStyles
} from '@material-ui/core';

import { stableSort, getSorting } from '../utility/sort';
import { MOCK_SENDS } from '../mock/sends';

const useStyles = makeStyles(theme => {
    return ({
        header: {
            paddingBottom: theme.spacing(1)
        },
        sendBox: {
            marginTop: theme.spacing(2),
            marginBottom: theme.spacing(2)
        },
    });
});

const headers = [
    { id: 'name', label: 'Name' },
    { id: 'type', label: 'Type' },
    { id: 'grade', label: 'Grade' },
    { id: 'style', label: 'Style' },
    { id: 'location', label: 'Location' }
];

function SendTableHeader(props) {
    const { order, orderBy, onRequestSort } = props;

    return (
      <TableHead>
        <TableRow>
          {headers.map((header, index) => (
            <TableCell
              key={header.id}
              align={index === 0 ? 'left' : 'right'}
              sortDirection={orderBy === header.id ? order : false}
            >
              <TableSortLabel
                active={orderBy === header.id}
                direction={order}
                onClick={() => onRequestSort(header.id)}
              >
                {header.label}
              </TableSortLabel>
            </TableCell>
          ))}
        </TableRow>
      </TableHead>
    );
}

SendTableHeader.propTypes = {
    order: PropTypes.oneOf(['asc', 'desc']).isRequired,
    orderBy: PropTypes.string.isRequired,
    onRequestSort: PropTypes.func.isRequired,
};

export default () => {
    const classes = useStyles();
    const [sends] = React.useState(MOCK_SENDS);
    const [order, setOrder] = React.useState('asc');
    const [orderBy, setOrderBy] = React.useState('name');

    const handleSortRequest = property => {
        const isDesc = orderBy === property && order === 'desc';
        setOrder(isDesc ? 'asc' : 'desc');
        setOrderBy(property);
    };

    return (
        <React.Fragment>
            <Typography className={classes.header} variant="h4" align="left">
                My Sends
            </Typography>
            <Paper className={classes.sendBox}>
                <Table>
                    <SendTableHeader
                        order={order}
                        orderBy={orderBy}
                        onRequestSort={handleSortRequest}
                    />
                    <TableBody>
                        {stableSort(sends, getSorting(order, orderBy)).map(s => (
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