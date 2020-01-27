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
    Grow
} from '@material-ui/core';
import { FilterListRounded as FilterIcon } from '@material-ui/icons';

import { stableSort, getSorting, SortOrder } from '../utility/sort';
import { Style } from '../models/send';
import { MOCK_SENDS } from '../mock/sends';

const useStyles = makeStyles(theme => {
    return ({
        header: {
            paddingBottom: theme.spacing(1),
        },
        filterToolbar: {
            height: 80
        },
        filterForm: {
            '& > *': {
                margin: theme.spacing(1),
                width: 120,
            }
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

type HeaderId = 'name' | 'style' | 'grade' | 'tickType' | 'location';

const headers: { id: HeaderId, label: string }[] = [
    { id: 'name', label: 'Name' },
    { id: 'style', label: 'Style' },
    { id: 'grade', label: 'Grade' },
    { id: 'tickType', label: 'Tick Type' },
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

interface SendFilterProps {
}

function SendFilter(props: SendFilterProps) {
    const classes = useStyles(props);
    const [filter, setFilter] = React.useState({
        name: '',
        style: 'all',
        grade: '',
        tickType: 'all',
        location: ''
    });

    const inputLabel = React.useRef(null);
    const [labelWidth, setLabelWidth] = React.useState(40);
    React.useEffect(() => {
      setLabelWidth(inputLabel.current.offsetWidth);
    }, []);

    type inputChangeEvent = React.ChangeEvent<({ name: string, value: string })>;
    const handleFilterChange = (event: inputChangeEvent) => {
        console.log(event.target);
        setFilter({
        ...filter,
        [event.target.name]: event.target.value
    })};

    return (
            <form className={classes.filterForm} autoComplete="off">
                <TextField
                    label="Name"
                    name="name"
                    variant="outlined"
                    value={filter.name}
                    InputLabelProps={{ shrink: true }}
                    onChange={handleFilterChange}
                />
                <FormControl variant="outlined">
                    <InputLabel
                        shrink 
                        ref={inputLabel}
                        htmlFor="style-filter"
                    >
                        <Typography>Style</Typography>
                    </InputLabel>
                    <Select 
                        id="style-filter"
                        value={filter.style} 
                        onChange={handleFilterChange}
                        input={
                            <OutlinedInput
                                notched
                                id="style-filter"
                                name="style"
                                labelWidth={labelWidth}
                            />
                        }
                    >
                        <MenuItem value="all">All</MenuItem>
                        <MenuItem value={Style.BOULDER}>{Style.BOULDER}</MenuItem>
                        <MenuItem value={Style.SPORT}>{Style.SPORT}</MenuItem>
                        <MenuItem value={Style.TRAD}>{Style.TRAD}</MenuItem>
                    </Select>
                </FormControl>
                <TextField
                    label="Grade"
                    name="grade"
                    variant="outlined"
                    value={filter.grade}
                    InputLabelProps={{ shrink: true }}
                    onChange={handleFilterChange}
                />
                <FormControl variant="outlined">
                    <InputLabel
                        shrink 
                        ref={inputLabel}
                        htmlFor="tickType-filter"
                    >
                        <Typography>Style</Typography>
                    </InputLabel>
                    <Select 
                        id="tickType-filter"
                        value={filter.tickType} 
                        onChange={handleFilterChange}
                        input={
                            <OutlinedInput
                                notched
                                id="tickType-filter"
                                name="tickType"
                                labelWidth={labelWidth}
                            />
                        }
                    >
                        <MenuItem value="all">All</MenuItem>
                        <MenuItem value={Style.BOULDER}>{Style.BOULDER}</MenuItem>
                        <MenuItem value={Style.SPORT}>{Style.SPORT}</MenuItem>
                        <MenuItem value={Style.TRAD}>{Style.TRAD}</MenuItem>
                    </Select>
                </FormControl>
                <TextField
                    label="Location"
                    name="location"
                    variant="outlined"
                    value={filter.location}
                    InputLabelProps={{ shrink: true }}
                    onChange={handleFilterChange}
                />
            </form>
    )
}

export default (props: {}) => {
    const classes = useStyles(props);
    const [sends] = React.useState(MOCK_SENDS);
    const [filterVisible, setFilterVisible] = React.useState(false);
    const [order, setOrder] = React.useState<SortOrder>('asc');
    const [orderBy, setOrderBy] = React.useState<HeaderId>('name');
    const [page, setPage] = React.useState(0);

    const ROWS_PER_PAGE = 10;

    const handleSortRequest = (property: HeaderId) => {
        const isDesc = orderBy === property && order === 'desc';
        setOrder(isDesc ? 'asc' : 'desc');
        setOrderBy(property);
    };

    const handleChangePage = (_event: any, newPage: number) => {
        setPage(newPage);
    }

    return (
        <Box className={classes.sendBox}>
            <Typography className={classes.header} variant="h4" align="left">
                My Sends
            </Typography>
            <Paper className={classes.table}>
                <Toolbar className={classes.filterToolbar}>
                    <Grow in={filterVisible}>
                        <div>
                            <SendFilter/>
                        </div>
                    </Grow>
                    <Tooltip className={classes.filterIcon} title={filterVisible ? 'Hide Filter' : 'Show Filter'}>
                        <IconButton aria-label="filter" onClick={() => setFilterVisible(!filterVisible)} >
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
                                    <TableCell align="right">{s.style}</TableCell>
                                    <TableCell align="right">{s.grade}</TableCell>
                                    <TableCell align="right">{s.tickType}</TableCell>
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