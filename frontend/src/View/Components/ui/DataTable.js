import React, {useEffect, useState} from "react";

import {
    useTable,
    useFilters,
    useGlobalFilter,
    useAsyncDebounce,
    usePagination,
    useSortBy,
    useRowState
} from 'react-table'
import Button from "./Button";
import SweetAlert from "react-bootstrap-sweetalert";
import {useTranslation, withTranslation} from "react-i18next";
import axios from '../util/Api';
import * as Constants from "../util/Constants";
import Loader from "react-loader-spinner";

const baseUrl = Constants.SERVER_URL;

function GlobalFilter({globalFilter, setGlobalFilter}) {
    const [value, setValue] = React.useState(globalFilter)
    const onChange = useAsyncDebounce(value => {
        setGlobalFilter(value || undefined)
    }, 200)

    return (
        <span>
            Global search:{' '}
            <input
                className="form-control"
                value={value || ""}
                onChange={e => {
                    setValue(e.target.value);
                    onChange(e.target.value);
                }}
            />
        </span>
    )
}

function DefaultColumnFilter({column: {filterValue, setFilter}}) {
    return (
        <input
            className="form-control"
            value={filterValue || ''}
            style={{"width": "100px"}}
            onChange={e => {
                setFilter(e.target.value || undefined)
            }}
        />
    )
}

function Table({columns, data, operations}) {
    const [deleteClicked, setDeleteClicked] = React.useState(false)
    const [id, setId] = React.useState(-1)
    const [url, setUrl] = React.useState("")

    const defaultColumn = React.useMemo(() => ({Filter: DefaultColumnFilter}), [])

    const {t} = useTranslation();
    const columnStyle = {verticalAlign: "middle"};

    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        prepareRow,
        page,
        canPreviousPage,
        canNextPage,
        pageOptions,
        pageCount,
        gotoPage,
        nextPage,
        previousPage,
        setPageSize,
        state: {pageIndex, pageSize},
        state,
        preGlobalFilteredRows,
        setGlobalFilter,
    } = useTable(
        {
            columns,
            data,
            defaultColumn,
            initialState: {pageIndex: 0, pageSize: 10},
            autoResetRowState: true,
            autoResetPage: true
        },
        useFilters,
        useGlobalFilter,
        useSortBy,
        usePagination,
    )

    function handleDeleteOperation(url, elementId) {
        setId(elementId)
        setUrl(url.replace("{id}", elementId))
        setDeleteClicked(true)
    }

    function deleteEntity(url, id) {
        // axios.delete(`${baseUrl}/${url}`).then(result => {
        data = data.filter(row => row.id !== id)
        // })
    }

    return (
        <div className={"w3-responsive"}>
            <GlobalFilter
                preGlobalFilteredRows={preGlobalFilteredRows}
                globalFilter={state.globalFilter}
                setGlobalFilter={setGlobalFilter}
            />
            <table className="w3-table-all w3-large w3-centered w3-hoverable" {...getTableProps()}>
                <thead>
                {headerGroups.map(headerGroup => (
                    <>
                        <tr {...headerGroup.getHeaderGroupProps()} className={"w3-light-grey"}>
                            {headerGroup.headers.map(column => (
                                <th {...column.getHeaderProps(column.getSortByToggleProps())}>
                                <span>
                                    {column.render('Header')}
                                    {column.isSorted
                                        ? column.isSortedDesc
                                            ? ' 🔽'
                                            : ' 🔼'
                                        : ''}
                                </span>
                                </th>
                            ))}
                        </tr>
                        <tr {...headerGroup.getHeaderGroupProps()} className={"w3-light-grey"}>
                            {headerGroup.headers.map(column =>
                                <th>
                                    {column.canFilter && column.id !== 'actions' ? column.render('Filter') : null}
                                </th>
                            )}
                        </tr>
                    </>
                ))}
                </thead>
                <tbody {...getTableBodyProps()}>
                {page.map((row) => {
                    prepareRow(row)
                    return (
                        <tr {...row.getRowProps()} className="w3-hover-sand">
                            {row.cells.map(cell => {
                                return <td style={columnStyle} {...cell.getCellProps()}>{cell.render('Cell')}</td>
                            })}
                            <td style={columnStyle}>
                                {operations.map(operation => {
                                    return <><Button
                                        className={operation.className}
                                        text={operation.name}
                                        onClick={() => {
                                            if (operation.name === 'Delete') {
                                                handleDeleteOperation(operation.url,
                                                    row.original[operation.onClickPassParameter])
                                            } else {
                                                operation.onClick(row.original[operation.onClickPassParameter])
                                            }
                                        }}
                                    /> &nbsp;
                                    </>
                                })}
                            </td>
                        </tr>
                    )
                })}
                </tbody>
            </table>
            <div className="w3-center">
                <ul className="w3-bar w3-large">
                    <li className="w3-button w3-bar-item w3-hover-light-grey" onClick={() => gotoPage(0)}
                        disabled={!canPreviousPage}>
                        <a>{'<<'}</a>
                    </li>
                    <li className="w3-button w3-bar-item w3-hover-light-grey" onClick={() => previousPage()}
                        disabled={!canPreviousPage}>
                        <a>{'<'}</a>
                    </li>
                    <li className="w3-button w3-bar-item w3-hover-light-grey" onClick={() => nextPage()}
                        disabled={!canNextPage}>
                        <a>{'>'}</a>
                    </li>
                    <li className="w3-button w3-bar-item w3-hover-light-grey" onClick={() => gotoPage(pageCount - 1)}
                        disabled={!canNextPage}>
                        <a>{'>>'}</a>
                    </li>
                    <li className="w3-button w3-bar-item w3-hover-light-grey">
                        <a>
                            Page{' '}
                            <strong>
                                {pageIndex + 1} of {pageOptions.length}
                            </strong>{' '}
                        </a>
                    </li>
                    <li className="w3-button w3-bar-item w3-hover-light-grey">
                        <a>
                            <input
                                className="form-control"
                                type="number"
                                min={1}
                                max={pageOptions.length}
                                defaultValue={pageIndex + 1}
                                onChange={e => {
                                    const page = e.target.value ? Number(e.target.value) - 1 : 0
                                    gotoPage(page)
                                }}
                                style={{width: '100px', height: '20px'}}
                            />
                        </a>
                    </li>
                    {' '}
                    <select
                        className="w3-button w3-bar-item w3-hover-light-grey"
                        value={pageSize}
                        onChange={e => {
                            setPageSize(Number(e.target.value))
                        }}
                        style={{width: '120px', height: '38px'}}
                    >
                        {[5, 10, 20, 30, 40, 50].map(pageSize => (
                            <option key={pageSize} value={pageSize}>
                                Show {pageSize}
                            </option>
                        ))}
                    </select>
                </ul>
            </div>
            {deleteClicked && <SweetAlert
                danger
                dependencies={[deleteClicked]}
                title={t("AreYouSure")}
                customButtons={
                    <React.Fragment>
                        <button
                            className="w3-btn w3-light-grey w3-round-small w3-medium"
                            onClick={() => setDeleteClicked(false)}
                        >{"Cancel"}</button>
                        &nbsp;
                        <button
                            className="w3-btn w3-red w3-round-small w3-medium"
                            onClick={() => {
                                alert("Deleted " + id + " on path " + url)
                                setDeleteClicked(false)
                                deleteEntity(url, id)
                            }
                            }
                        >{"Delete"}</button>
                    </React.Fragment>
                }
            >
                {"Cannot recover"}
            </SweetAlert>}
        </div>
    )
}

function DataTableComponent({getDataUrl, displayColumns, operations}) {

    const [data, setData] = useState([])
    const [isLoaded, setIsLoaded] = useState(false)

    useEffect(() => {
        axios.get(getDataUrl)
            .then(result => result.data)
            .then(data => {
                console.log(data)
                setData([...data])
                setIsLoaded(true)
            })
    }, [])

    const columns = React.useMemo(
        () => [
            ...displayColumns,
            {
                id: 'actions',
                accessor: 'actions',

                Cell: (row) => (
                    <span style={{cursor: 'pointer', color: 'blue', textDecoration: 'underline'}}
                          onClick={() => {
                              let id = row.row.original.id
                              let index = data.findIndex(entry => entry.id == id);
                              data.splice(index, 1)
                              setData(data.filter(entry => entry.id != id))
                          }}
                    >Delete</span>
                )
            }
        ],
        []
    )

    if (isLoaded) {
        return (
            <Table columns={columns} data={data} operations={operations}/>
        )
    } else {
        return <div className="centered">
            <Loader
                type="Oval" //Audio Oval ThreeDots
                color="#4B0082"
                height={325}
                width={325}
                timeout={10000}
            />
        </div>
    }
}

// export default withTranslation()(DataTableComponent);

function DataTableUsageExample() {

    const {t} = useTranslation();
    const dataUrl = `${baseUrl}/placement-owners/placement.owner@gmail.com/placements`

    const columns = React.useMemo(
        () => [
            {
                Header: 'ID',
                accessor: 'id',
            },
            {
                Header: t("Type"),
                accessor: 'placementType',
            },
            {
                Header: t("Floor"),
                accessor: 'floor'
            },
            {
                Header: t("WCount"),
                accessor: 'windowsCount'
            },
            {
                Header: t("Area"),
                accessor: 'area'
            },
            {
                Header: t("Date"),
                accessor: 'lastCleaning'
            }
        ],
        []
    )

    function editRoom(id) {
        localStorage.setItem("roomId", id);
        window.location.href = "./edit_room";
    }

    const operations = [
        {
            "name": "Delete",
            "className": "w3-btn w3-red w3-round-small w3-medium",
            "onClickPassParameter": "id",
            "url": "placement-owners/placements/{id}",
        },
        {
            "name": "Edit",
            "onClick": editRoom,
            "className": "w3-btn w3-khaki w3-round-small w3-medium",
            "onClickPassParameter": "id"
        },
    ]

    return (
        <DataTableComponent getDataUrl={dataUrl} displayColumns={columns} operations={operations}/>
    )
}

export default withTranslation()(DataTableUsageExample);
