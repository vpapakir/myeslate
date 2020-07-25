package gr.cti.eslate.utils.browser;

import java.util.*;

/**
 * Greek language localized strings for ESlateBrowser and its subclasses.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.12, 31-Oct-2006
 */
public class ESlateBrowserResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // Location
    {"changedLocation", "������ �����"},
    {"changingLocation", "����������� ������ �����"},
    // Title
    {"changedTitle", "������ ������"},
    // Status text
    {"changedStatusText", "������ �������� ����������"},
    // WebBrowserEvents
    {"documentCompleted", "���������� ��������"},
    {"downloadCompleted", "���������� �����"},
    {"downloadError", "������ �����"},
    {"downloadProgress", "������� �����"},
    {"downloadStarted", "�������� �����"},
    {"statusTextChange", "������ �������� ����������"},
    {"titleChange", "������ ������"},
    // HistoryChanged
    {"historyChanged", "�������� ���������"},
    // LinkFollowed
    {"linkFollowed", "���������� ���������"}
  };
}
