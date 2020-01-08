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
} from '@material-ui/core';
import { FilterListRounded as FilterIcon } from '@material-ui/icons';

import { stableSort, getSorting, SortOrder } from '../utility/sort';
import { MOCK_SENDS } from '../mock/sends';

const useStyles = makeStyles(theme => {
    return ({
        header: {
            paddingBottom: theme.spacing(1),
        },
        filterIcon: {
            position: 'absolute',
            right: theme.spacing(1),
        },
        sendBox: {
            margin: 'auto',
            [theme.breakpoints.down('md')]: {
                width: '100%'
            },
            [theme.breakpoints.up('md')]: {
                width: '800px',
            },
        },
        table: {
            marginTop: theme.spacing(2)
        }
    });
});

type HeaderId = 'name' | 'type' | 'grade' | 'style' | 'location';

const headers: { id: HeaderId, label: string }[] = [
    { id: 'name', label: 'Name' },
    { id: 'type', label: 'Type' },
    { id: 'grade', label: 'Grade' },
    { id: 'style', label: 'Style' },
    { id: 'location', label: 'Location' }
];

interface SendTableHeaderProps {
    order: SortOrder,
    orderBy: HeaderId,
    onRequestSort: (id: HeaderId) => void
}

function SendTableHeader(props: SendTableHeaderProps) {
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

export default (props: {}) => {
    const classes = useStyles(props);
    const [sends] = React.useState(MOCK_SENDS);
    const [order, setOrder] = React.useState<SortOrder>('asc');
    const [orderBy, setOrderBy] = React.useState<HeaderId>('name');
    const [page, setPage] = React.useState(0);
    const ROWS_PER_PAGE = 10;

    const handleSortRequest = (property: HeaderId) => {
        const isDesc = orderBy === property && order === 'desc';
        setOrder(isDesc ? 'asc' : 'desc');
        setOrderBy(property);
    };

    const handleChangePage = (event: any, newPage: number) => {
        setPage(newPage);
    }

    return (
        <Box className={classes.sendBox}>
            <Typography className={classes.header} variant="h4" align="left">
                My Sends
            </Typography>
            <Paper className={classes.table}>
                <Toolbar>
                    <Tooltip className={classes.filterIcon} title="Filter Items">
                        <IconButton aria-label="filter">
                            <FilterIcon/>
                        </IconButton>
                    </Tooltip>
                </Toolbar>
                <Table>
                    <SendTableHeader
                        order={order}
                        orderBy={orderBy}
                        onRequestSort={handleSortRequest}
                    />
                    <TableBody>
                        {stableSort(sends, getSorting(order, orderBy))
                            .slice(page * ROWS_PER_PAGE, (page + 1) * ROWS_PER_PAGE)
                            .map(s => (
                                <TableRow key={`${s.name}-${s.location}`}>
                                    <TableCell component="th" scope="row">{s.name}</TableCell>
                                    <TableCell align="right">{s.type}</TableCell>
                                    <TableCell align="right">{s.grade}</TableCell>
                                    <TableCell align="right">{s.style}</TableCell>
                                    <TableCell align="right">{s.location}</TableCell>
                                </TableRow>
                            )
                        )}
                    </TableBody>
                </Table>
                <TablePagination
                    component="div"
                    count={sends.length}
                    rowsPerPage={ROWS_PER_PAGE}
                    rowsPerPageOptions={[]}
                    page={page}
                    onChangePage={handleChangePage}
                />
            </Paper>
        </Box>
    );
};