// @flow

import React from 'react'
import { Cell, Column, ColumnHeaderCell } from '@blueprintjs/table'
import { Menu, MenuItem } from '@blueprintjs/core'

export type CellLookupType = (rowIndex: number, columnIndex: number) => React$Node
export type SortCallbackType<T> = (columnIndex: number, comparator: (a: T, b: T) => number) => void

export interface ISortableColumn<T> {
  getColumn (getCellData: CellLookupType, sortColumn: SortCallbackType<T>): React$Node
}

type MenuRendererType = (name: string, index: string) => React$Node

class SortableColumn<T> implements ISortableColumn<T> {
  name: string
  index: number
  menuRenderer: MenuRendererType

  constructor (name: string, index: number, menuRenderer: MenuRendererType) {
    this.name = name
    this.index = index
    this.menuRenderer = menuRenderer
  }

  createHeaderRenderer = (name: string) => () => <ColumnHeaderCell name={name} menuRenderer={this.menuRenderer}/>
  createCellRenderer = (getCellData: CellLookupType) => (row, col) => <Cell>{getCellData(row, col)}</Cell>

  getColumn (getCellData: CellLookupType, sortColumn: SortCallbackType<T>) {
    const cellRenderer = this.createCellRenderer(getCellData)
    return (
      <Column
        cellRenderer={cellRenderer}
        columnHeaderCellRenderer={this.createHeaderRenderer(this.name)}
        key={this.index}
        name={this.name}
      />
    )
  }
}

const TextColumnMenuRenderer = () => {
  const sortAsc = () => sortColumn(this.index, (a, b) => this.compare(a, b))
  const sortDesc = () => sortColumn(this.index, (a, b) => this.compare(b, a))
  return (
    <Menu>
      <MenuItem icon='sort-asc' onClick={sortAsc} text='Sort Asc'/>
      <MenuItem icon='sort-desc' onClick={sortDesc} text='Sort Desc'/>
    </Menu>
  )
}

const textColumnMenuRenderer = (name: string, index: number, sortColumn: (index: number, sort: (string, string) => number) => void) => {
  const sortAsc = () => sortColumn(index, (a, b) => a.localeCompare(b))
  const sortDesc = () => sortColumn(index, (a, b) => b.localeCompare(a))
  return (
    <Menu>
      <MenuItem icon='sort-asc' onClick={sortAsc} text='Sort Asc'/>
      <MenuItem icon='sort-desc' onClick={sortDesc} text='Sort Desc'/>
    </Menu>
  )
}

class TextSortableColumn extends SortableColumn<string> {
  constructor (name: string, index: number) {
    super(name, index, textColumnMenuRenderer)
  }
}
